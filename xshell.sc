// SN/X Shell
// Note: Global variables should be at the head (maybe).

// ==================== Global variables ====================
int START_MSG[6];
int CMD_PRINT[6];
int CMD_OMIKUJI[8];
int DAIKICHI[9];
int CHUKICHI[9];
int SHOKICHI[9];
int CMD_NONE[18];
int CMD_LET[4];
int VARIABLES[5];
int tmp_buffer[256];
int input_buffer[512];

BUFFER_SIZE = 512;

CMD_LET[0] = 0x4c;
CMD_LET[1] = 0x45;
CMD_LET[2] = 0x54;
CMD_LET[3] = 0x00;

START_MSG[0] = 0x53;
START_MSG[1] = 0x54;
START_MSG[2] = 0x41;
START_MSG[3] = 0x52;
START_MSG[4] = 0x54;
START_MSG[5] = 0x00;

CMD_PRINT[0] = 0x50;
CMD_PRINT[1] = 0x52;
CMD_PRINT[2] = 0x49;
CMD_PRINT[3] = 0x4e;
CMD_PRINT[4] = 0x54;
CMD_PRINT[5] = 0x00;

CMD_OMIKUJI[0] = 0x4f;
CMD_OMIKUJI[1] = 0x4d;
CMD_OMIKUJI[2] = 0x49;
CMD_OMIKUJI[3] = 0x4b;
CMD_OMIKUJI[4] = 0x55;
CMD_OMIKUJI[5] = 0x4a;
CMD_OMIKUJI[6] = 0x49;
CMD_OMIKUJI[7] = 0x00;

CMD_NONE[0] = 0x2a;
CMD_NONE[1] = 0x49;
CMD_NONE[2] = 0x4e;
CMD_NONE[3] = 0x56;
CMD_NONE[4] = 0x41;
CMD_NONE[5] = 0x4c;
CMD_NONE[6] = 0x49;
CMD_NONE[7] = 0x44;
CMD_NONE[8] = 0x20;
CMD_NONE[9] = 0x43;
CMD_NONE[10] = 0x4f;
CMD_NONE[11] = 0x4d;
CMD_NONE[12] = 0x4d;
CMD_NONE[13] = 0x41;
CMD_NONE[14] = 0x4e;
CMD_NONE[15] = 0x44;
CMD_NONE[16] = 0x2a;
CMD_NONE[17] = 0x00;

// Peripheral addrs.
LCD    = 0xA020;     // LCD
PS2DAT = 0xA004;  // PS2
PS2IN  = 0xA005;
LEDG   = 0xA003;  // LED

LCD_MAX_COLUMN = 16;
LCD_MAX_ROW    = 2;
lcd_current_x  = 0;
lcd_current_y  = 0;

ps2_on_break = 0;
ps2_is_shift = 0;


// ==================== Global variables ====================
#include "display_vga.sc"
#include "utils.sc"


// ==================== Functions ====================
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


void clear()
{
    vga_clear();
    lcd_clear();
}


void putchar(ch)
{
    vga_putchar(ch);
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


void print_num(num_print)
{
    itoa(num_print, &tmp_buffer);
    print_str(&tmp_buffer);
}


void println_num(num_print)
{
    print_num(num_print);
    putchar(0x0A);
}


int decode_key(key_code)
{
    int ascii_code;
    ascii_code = 0;

    if ((key_code == 0x59) || (key_code == 0x12)) {
        // Handle shift key.
        if (ps2_on_break == 1) {
            ps2_is_shift = 0;
        } else {
            ps2_is_shift = 1;
        }
    }

    if (ps2_on_break == 1) {
        ps2_on_break = 0;
        key_code = 0x00;
        // Avoid return here, because it may cause fault (WTF!!!).
    }

    if (key_code == 0xFA) { ascii_code = 0x00; }
    else if (key_code == 0xF0) { ps2_on_break = 1;  }  // Break code.
    else if (ps2_is_shift == 1) {
        if (key_code == 0x16) { ascii_code = 0x21; } // '!'
        else if (key_code == 0x1E) { ascii_code = 0x22; } // '"'
        else if (key_code == 0x26) { ascii_code = 0x23; } // '#'
        else if (key_code == 0x25) { ascii_code = 0x24; } // '$'
        else if (key_code == 0x2E) { ascii_code = 0x25; } // '%'
        else if (key_code == 0x36) { ascii_code = 0x26; } // '&'
        else if (key_code == 0x3D) { ascii_code = 0x27; } // '''
        else if (key_code == 0x3E) { ascii_code = 0x28; } // '('
        else if (key_code == 0x46) { ascii_code = 0x29; } // ')'
        else if (key_code == 0x4E) { ascii_code = 0x3D; } // '='
        else if (key_code == 0x55) { ascii_code = 0x7E; } // '~'
        else if (key_code == 0x6A) { ascii_code = 0x7C; } // '|'
        else if (key_code == 0x54) { ascii_code = 0x60; } // '`'
        else if (key_code == 0x5B) { ascii_code = 0x7B; } // '{'
        else if (key_code == 0x4C) { ascii_code = 0x2B; } // '+'
        else if (key_code == 0x52) { ascii_code = 0x2A; } // '*'
        else if (key_code == 0x5D) { ascii_code = 0x7D; } // '}'
        else if (key_code == 0x41) { ascii_code = 0x3C; } // '<'
        else if (key_code == 0x49) { ascii_code = 0x3E; } // '>'
        else if (key_code == 0x4A) { ascii_code = 0x3F; } // '?'
        else if (key_code == 0x51) { ascii_code = 0x5F; } // '_'
    }
    else if (key_code == 0x5A) { ascii_code = 0x0A; }  // Enter.
    else if (key_code == 0x76) { ascii_code = 0x1B; }  // ESC
    else if (key_code == 0x66) { ascii_code = 0x5C; }  // Backspace.
    else if (key_code == 0x29) { ascii_code = 0x20; }  // Space.
    else if (key_code == 0x0D) { ascii_code = 0x09; }  // Tab.
    else if (key_code == 0x1C) { ascii_code = 0x61; }  // 'a'
    else if (key_code == 0x32) { ascii_code = 0x62; }  // 'b'
    else if (key_code == 0x21) { ascii_code = 0x63; }  // 'c'
    else if (key_code == 0x23) { ascii_code = 0x64; }  // 'd'
    else if (key_code == 0x24) { ascii_code = 0x65; }  // 'e'
    else if (key_code == 0x2B) { ascii_code = 0x66; }  // 'f'
    else if (key_code == 0x34) { ascii_code = 0x67; }  // 'g'
    else if (key_code == 0x33) { ascii_code = 0x68; }  // 'h'
    else if (key_code == 0x43) { ascii_code = 0x69; }  // 'i'
    else if (key_code == 0x3B) { ascii_code = 0x6A; }  // 'j'
    else if (key_code == 0x42) { ascii_code = 0x6B; }  // 'k'
    else if (key_code == 0x4B) { ascii_code = 0x6C; }  // 'l'
    else if (key_code == 0x3A) { ascii_code = 0x6D; }  // 'm'
    else if (key_code == 0x31) { ascii_code = 0x6E; }  // 'n'
    else if (key_code == 0x44) { ascii_code = 0x6F; }  // 'o'
    else if (key_code == 0x4D) { ascii_code = 0x70; }  // 'p'
    else if (key_code == 0x15) { ascii_code = 0x71; }  // 'q'
    else if (key_code == 0x2D) { ascii_code = 0x72; }  // 'r'
    else if (key_code == 0x1B) { ascii_code = 0x73; }  // 's'
    else if (key_code == 0x2C) { ascii_code = 0x74; }  // 't'
    else if (key_code == 0x3C) { ascii_code = 0x75; }  // 'u'
    else if (key_code == 0x2A) { ascii_code = 0x76; }  // 'v'
    else if (key_code == 0x1D) { ascii_code = 0x77; }  // 'w'
    else if (key_code == 0x22) { ascii_code = 0x78; }  // 'x'
    else if (key_code == 0x35) { ascii_code = 0x79; }  // 'y'
    else if (key_code == 0x1A) { ascii_code = 0x7A; }  // 'z'
    else if (key_code == 0x45) { ascii_code = 0x30; }  // '0'
    else if (key_code == 0x16) { ascii_code = 0x31; }  // '1'
    else if (key_code == 0x1E) { ascii_code = 0x32; }  // '2'
    else if (key_code == 0x26) { ascii_code = 0x33; }  // '3'
    else if (key_code == 0x25) { ascii_code = 0x34; }  // '4'
    else if (key_code == 0x2E) { ascii_code = 0x35; }  // '5'
    else if (key_code == 0x36) { ascii_code = 0x36; }  // '6'
    else if (key_code == 0x3D) { ascii_code = 0x37; }  // '7'
    else if (key_code == 0x3E) { ascii_code = 0x38; }  // '8'
    else if (key_code == 0x46) { ascii_code = 0x39; }  // '9'
    else if (key_code == 0x49) { ascii_code = 0x2E; }  // '.'
    else if (key_code == 0x4A) { ascii_code = 0x2F; }  // '/'
    else if (key_code == 0x0E) { ascii_code = 0x60; }  // '`'
    else if (key_code == 0x4E) { ascii_code = 0x2D; }  // '-'
    else if (key_code == 0x55) { ascii_code = 0x3D; }  // '='
    else if (key_code == 0x79) { ascii_code = 0x2B; }  // '+' num key

    return ascii_code;
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


int has_arithmetic_ops(ptr_arith)
{
    int has_operator;
    has_operator = strchr(ptr_arith, 0x2A) + strchr(ptr_arith, 0x2B) + strchr(ptr_arith, 0x2D) + strchr(ptr_arith, 0x2F);
    if (has_operator != 0) {
        return 1;
    } else {
        return 0;
    }
}


int is_variable_char(var_ch)
{
    if ((0x41 <= var_ch) && (var_ch <= 0x5A)) {
        return 1;
    } else {
        return 0;
    }
}


int evaluate_expression(ptr_eval)
{
    int ptr_operator;
    int prev_operator;
    int ptr_tmp;
    int val_tmp;
    int store_val;
    int result_value;
    int state;
    int STATE_ARITHMETIC;

    STATE_ARITHMETIC = 1;
    STATE_VAR_ASSIGN = 2;
    state            = 0;
    result_value     = 0;

    // '*' -> 0x2A
    // '+' -> 0x2B
    // '-' -> 0x2D
    // '/' -> 0x2F
    if (is_variable_char(*ptr_eval) == 1 && 1 < strlen(ptr_eval)) {
        state = STATE_VAR_ASSIGN;
        ptr_eval = ptr_eval + 2;
    } else if (has_arithmetic_ops(ptr_eval) != 0 || is_number_str(ptr_eval) == 1) {
        state = STATE_ARITHMETIC;
    }

    if (state == STATE_ARITHMETIC || state == STATE_VAR_ASSIGN) {
        trim_spaces(ptr_eval);

        result_value = 0;
        prev_operator = 0;
        while (1) {
            ptr_operator = strchr(ptr_eval, 0x2A);

            ptr_tmp = strchr(ptr_eval, 0x2B);
            if (ptr_operator == 0) {
                ptr_operator = ptr_tmp;
            } else if ((ptr_tmp != 0) && (ptr_tmp < ptr_operator)) {
                ptr_operator = ptr_tmp;
            }

            ptr_tmp = strchr(ptr_eval, 0x2D);
            if (ptr_operator == 0) {
                ptr_operator = ptr_tmp;
            } else if ((ptr_tmp != 0) && (ptr_tmp < ptr_operator)) {
                ptr_operator = ptr_tmp;
            }

            ptr_tmp = strchr(ptr_eval, 0x2F);
            if (ptr_operator == 0) {
                ptr_operator = ptr_tmp;
            } else if ((ptr_tmp != 0) && (ptr_tmp < ptr_operator)) {
                ptr_operator = ptr_tmp;
            }

            // Read operand.
            if (ptr_operator == 0) {
                // Last operand.
                val_tmp = atoi(ptr_eval);
            } else {
                store_val     = *ptr_operator;
                *ptr_operator = 0x00;
                val_tmp       = atoi(ptr_eval);
                *ptr_operator = store_val;
            }

            if (prev_operator == 0) {
                // First operand.
                result_value = val_tmp;
            } else if (0x2A == prev_operator) {
                result_value = result_value * val_tmp;
            } else if (0x2B == prev_operator) {
                result_value = result_value + val_tmp;
            } else if (0x2D == prev_operator) {
                result_value = result_value - val_tmp;
            } else if (0x2F == prev_operator) {
                result_value = div(result_value, val_tmp);
            }
            prev_operator = store_val;

            if (ptr_operator == 0) {
                break;
            }

            ptr_eval = ptr_operator + 1;
        }

        is_arithmetic = 1;
    } else {
        if (strlen(ptr_eval) == 1) {
            val_tmp = *ptr_eval;
            if ((0x41 <= val_tmp) && (val_tmp <= 0x5A)) {
                println_num(VARIABLES[val_tmp - 0x41]);
            }
        } else {
            println_str(ptr_eval);
            is_arithmetic = 0;
        }
    }

    return result_value;
}

#include "cmd_omikuji.sc"

int execute(cmd_ptr)
{
    int arg_ptr;
    int result;

    is_arithmetic  = 0;

    if (strncmp(cmd_ptr, &CMD_PRINT, 5) == 0) {
        arg_ptr = cmd_ptr + 6;
        result = evaluate_expression(arg_ptr);
        if (is_arithmetic == 1) {
            println_num(result);
        }
    } else if(strncmp(cmd_ptr, &CMD_LET, 3) == 0){
        // LET A 1 + 2 + 3
        arg_ptr = cmd_ptr + 4;
        VARIABLES[*arg_ptr - 0x41] = evaluate_expression(arg_ptr);
    } else if(strncmp(cmd_ptr, &CMD_OMIKUJI, 7) == 0){
        exe_omikuji();
    } else {
        println_str(&CMD_NONE);
    }

    return 1;
}


void main()
{
    int buf_idx;
    int input_char;
    int buf;

    input_buffer[BUFFER_SIZE] = 0x00;
    buf_idx = 0;

    // Initialize.
    led_set(0x00, 0xFF);
    clear();
    println_str(&START_MSG);

    putchar(0x25);
    putchar(0x20);

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
            input_buffer[buf_idx] = 0x00;
            execute(&input_buffer);

            putchar(0x25);
            putchar(0x20);

            buf_idx = 0;
        } else if ((input_char == 0x5C) && (0 < buf_idx)) {
            // Backspace.
            buf_idx--;
        } else {
            input_buffer[buf_idx] = input_char;
            buf_idx++;
        }

        if (buf_idx == BUFFER_SIZE) {
            buf_idx = 0;
        }
    }
}


// ==================== Main routine ====================
while (1) {
    main();
}
halt;
