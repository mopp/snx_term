// SN/X Shell
#include "display_vga.sc"
#include "utils.sc"

// ==================== Global variables ====================
int START_MSG[6];
int TEST_STR1[6];
int TEST_STR_MSG[7];
int CMD_PRINT[6];
int in_buffer[2400];

BUFFER_SIZE = 2400;

START_MSG[0] = 0x53;
START_MSG[1] = 0x54;
START_MSG[2] = 0x41;
START_MSG[3] = 0x52;
START_MSG[4] = 0x54;
START_MSG[5] = 0x00;

TEST_STR1[0] = 0x48;
TEST_STR1[1] = 0x45;
TEST_STR1[2] = 0x4C;
TEST_STR1[3] = 0x4C;
TEST_STR1[4] = 0x4F;
TEST_STR1[5] = 0x00;

TEST_STR_MSG[0] = 0x46;
TEST_STR_MSG[1] = 0x41;
TEST_STR_MSG[2] = 0x49;
TEST_STR_MSG[3] = 0x4c;
TEST_STR_MSG[4] = 0x45;
TEST_STR_MSG[5] = 0x44;
TEST_STR_MSG[6] = 0x00;

CMD_PRINT[0] = 0x50;
CMD_PRINT[1] = 0x52;
CMD_PRINT[2] = 0x49;
CMD_PRINT[3] = 0x4e;
CMD_PRINT[4] = 0x54;
CMD_PRINT[5] = 0x00;

// Peripheral addrs.
LCD = 0xA020;     // LCD
PS2DAT = 0xA004;  // PS2
PS2IN = 0xA005;

LCD_MAX_COLUMN = 16;
LCD_MAX_ROW = 2;
lcd_current_x = 0;
lcd_current_y = 0;

ps2_on_break = 0;


// ==================== Functions ====================

void do_tests()
{
    int cnt;

    cnt = 0;
    cnt = cnt + test_eq(strcmp(&TEST_STR1, &TEST_STR1), 0);
    cnt = cnt + test_neq(strcmp(&TEST_STR1, &TEST_STR_MSG), 0);
    cnt = cnt + test_eq(strncmp(&TEST_STR1, &TEST_STR1, 1), 0);
    cnt = cnt + test_eq(strncmp(&TEST_STR1, &TEST_STR1, 2), 0);
    cnt = cnt + test_neq(strncmp(&TEST_STR1, &TEST_STR_MSG, 2), 0);
    cnt = cnt + test_eq(modulo(10, 2), 0);
    cnt = cnt + test_eq(modulo(10, 3), 1);
    cnt = cnt + test_eq(modulo(200, 7), 4);
    cnt = cnt + test_eq(div(4, 4), 1);
    cnt = cnt + test_eq(div(12, 4), 3);
    cnt = cnt + test_eq(div(45, 5), 9);

    if (cnt != 0) {
        lcd_clear();
        print_str(&msg);

        halt;
    }
}

void lcd_putchar(ch)
{
    int second_line;

    if (ch == 0x0A) {
        // '\n'
        lcd_current_y = lcd_current_y + 1;
        lcd_current_x = 0;
    } else if (ch == 0x5C) {
        if (lcd_current_x != 0) {
            // '\b'
            lcd_current_x--;
            putchar(0x20);
            lcd_current_x--;
        }
    } else {
        *(LCD + ((lcd_current_y * LCD_MAX_COLUMN) + lcd_current_x)) = ch;
        lcd_current_x = lcd_current_x + 1;
    }

    if (lcd_current_x == LCD_MAX_COLUMN) {
        lcd_current_x = 0;
        lcd_current_y = lcd_current_y + 1;
    }

    if (LCD_MAX_ROW <= lcd_current_y) {
        lcd_current_y = 1;
        // Copy string from second line to firstline.
        // And clear second line.
        for (i = 0; i < LCD_MAX_COLUMN; i++) {
            second_line = (LCD + (LCD_MAX_COLUMN + i));
            *(LCD + i) = *second_line;
            *second_line = 0x20;
        }
    }
}


void lcd_clear()
{
    int idx;

    lcd_current_x = 0;
    lcd_current_y = 0;
    for (idx = 0; idx < (LCD_MAX_ROW * LCD_MAX_COLUMN); idx++) {
        *(LCD + idx) = 0x20;
    }
}


void putchar(ch)
{
    vga_put_char(ch);
}


void print_str(str_ptr)
{
    while (*str_ptr != 0x00) {
        putchar(*str_ptr);
        str_ptr++;
    }
}


void println_str(str_ptr)
{
    print_str(str_ptr);
    putchar(0x0A);
}


int decode_key(key_code)
{
    if (ps2_on_break == 1) {
        ps2_on_break = 0;
        return 0;
    }

    if (key_code == 0xFA) { return 0x00; }
    else if (key_code == 0xF0) { ps2_on_break = 1; }  // Break code.
    else if (key_code == 0x5A) { return 0x0A; }  // Enter.
    else if (key_code == 0x76) { return 0x1B; }  // ESC
    else if (key_code == 0x66) { return 0x5C; }  // Backspace.
    else if (key_code == 0x29) { return 0x20; }  // Space.
    else if (key_code == 0x0D) { return 0x09; }  // Tab.
    else if (key_code == 0x1C) { return 0x61; }  // 'a'
    else if (key_code == 0x32) { return 0x62; }  // 'b'
    else if (key_code == 0x21) { return 0x63; }  // 'c'
    else if (key_code == 0x23) { return 0x64; }  // 'd'
    else if (key_code == 0x24) { return 0x65; }  // 'e'
    else if (key_code == 0x2B) { return 0x66; }  // 'f'
    else if (key_code == 0x34) { return 0x67; }  // 'g'
    else if (key_code == 0x33) { return 0x68; }  // 'h'
    else if (key_code == 0x43) { return 0x69; }  // 'i'
    else if (key_code == 0x3B) { return 0x6A; }  // 'j'
    else if (key_code == 0x42) { return 0x6B; }  // 'k'
    else if (key_code == 0x4B) { return 0x6C; }  // 'l'
    else if (key_code == 0x3A) { return 0x6D; }  // 'm'
    else if (key_code == 0x31) { return 0x6E; }  // 'n'
    else if (key_code == 0x44) { return 0x6F; }  // 'o'
    else if (key_code == 0x4D) { return 0x70; }  // 'p'
    else if (key_code == 0x15) { return 0x71; }  // 'q'
    else if (key_code == 0x2D) { return 0x72; }  // 'r'
    else if (key_code == 0x1B) { return 0x73; }  // 's'
    else if (key_code == 0x2C) { return 0x74; }  // 't'
    else if (key_code == 0x3C) { return 0x75; }  // 'u'
    else if (key_code == 0x2A) { return 0x76; }  // 'v'
    else if (key_code == 0x1D) { return 0x77; }  // 'w'
    else if (key_code == 0x22) { return 0x78; }  // 'x'
    else if (key_code == 0x35) { return 0x79; }  // 'y'
    else if (key_code == 0x1A) { return 0x7A; }  // 'z'
    else if (key_code == 0x45) { return 0x30; }  // '0'
    else if (key_code == 0x16) { return 0x31; }  // '1'
    else if (key_code == 0x1E) { return 0x32; }  // '2'
    else if (key_code == 0x26) { return 0x33; }  // '3'
    else if (key_code == 0x25) { return 0x34; }  // '4'
    else if (key_code == 0x2E) { return 0x35; }  // '5'
    else if (key_code == 0x36) { return 0x36; }  // '6'
    else if (key_code == 0x3D) { return 0x37; }  // '7'
    else if (key_code == 0x3E) { return 0x38; }  // '8'
    else if (key_code == 0x46) { return 0x39; }  // '9'
    else if (key_code == 0x49) { return 0x2E; }  // '.'
    else if (key_code == 0x4A) { return 0x2F; }  // '/'
    else if (key_code == 0x0E) { return 0x60; }  // '`'
    else if (key_code == 0x4E) { return 0x2D; }  // '-'
    else if (key_code == 0x55) { return 0x3D; }  // '='

    return 0;
}


int getchar()
{
    int raw_key_code;
    int char_code;

    while (1) {
        if (*PS2IN) {
            raw_key_code = *PS2DAT;
            char_code = decode_key(raw_key_code);
            if (char_code != 0) {
                return char_code;
            }
        }
    }
}


int execute(buf_ptr, cmd_ptr)
{
    int arg_ptr;

    if (strncmp(cmd_ptr, &CMD_PRINT, 5) == 0) {
        arg_ptr = cmd_ptr + 6;
        println_str(arg_ptr);
        led_set(0x02, 0xFF);
    }

    return 1;
}


void main()
{
    int in_buf_idx;
    int input_char;
    int buf;

    in_buffer[BUFFER_SIZE] = 0x00;
    in_buf_idx = 0;

    // Initialize.
    led_set(0x00, 0xFF);
    lcd_clear();
    vga_clear();
    println_str(&START_MSG);

    while (1) {
        input_char = getchar();
        if (input_char == 0x1B) {
            // If ESC is passed, restart break to restart main routine.
            break;
        }

        // Print to LED.
        led_set(input_char, 0xFF);

        // Make char upper case.
        if ((0x61 <= input_char) && (input_char <= 0x7A)) {
            input_char = input_char - 0x20;
        }

        // Print to terminal.
        putchar(input_char);

        if (input_char == 0x0A) {
            // Newline.
            in_buffer[in_buf_idx] = 0x00;
            buf = (&in_buffer + prev_tail_idx);
            execute(&in_buffer, buf);

            in_buf_idx = 0;
        } else if ((input_char == 0x08) && (0 < in_buf_idx)) {
            // Backspace.
            in_buf_idx--;
        } else {
            in_buffer[in_buf_idx] = input_char;
            in_buf_idx++;
        }

        if (in_buf_idx == BUFFER_SIZE) {
            in_buf_idx = 0;
        }
    }
}


// ==================== Main routine ====================
do_tests();
while (1) {
    main();
}
halt;
