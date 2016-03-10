// SN/X Shell
#include "utils.sc"

// Peripheral addrs.
LCD    = 0xA020; // LCD
PS2DAT = 0xA004; // PS2
PS2IN  = 0xA005;


LCD_MAX_COLUMN = 16;
LCD_MAX_ROW    = 2;
lcd_current_x  = 0;
lcd_current_y  = 0;
void lcd_put_char(c)
{
    if (c == 0x0A) {
        // '\n'
        lcd_current_y = lcd_current_y + 1;
        lcd_current_x = 0;
    } else if(c == 0x5C) {
        if (lcd_current_x != 0) {
            // '\b'
            lcd_current_x--;
            lcd_put_char(0x20);
            lcd_current_x--;
        }
    } else {
        *(LCD + ((lcd_current_y * LCD_MAX_COLUMN) + lcd_current_x)) = c;
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
    lcd_current_x = 0;
    lcd_current_y = 0;
    for (i = 0; i < (LCD_MAX_ROW * LCD_MAX_COLUMN); i++) {
        *(LCD + i) = 0x20;
    }
}


void print_str(str_ptr)
{
    i = 0;
    while (*(str_ptr + i) != 0x00)
    {
        /* TODO: change LCD to VGA later. */
        lcd_put_char(*(str_ptr + i));
        i++;
    }
}


PS2_BREAK_CODE   = 0xf0;
PS2_ENTER        = 0x5A;
PS2_ESC          = 0x76;
PS2_BACKSPACE    = 0x66;
PS2_SPACE        = 0x29;
PS2_TAB          = 0x0D;
PS2_CAPS         = 0x58;
PS2_L_SHIFT      = 0x12;
PS2_L_ALT        = 0x11;
PS2_L_CTRL       = 0x14;
PS2_R_SHIFT      = 0x59;
PS2_BREAK_CODE   = 0xF0;
ps2_on_break     = 0;
int decode_key(key_code)
{
    if (ps2_on_break == 1) {
        ps2_on_break = 0;
        return 0;
    }

    if (key_code == 0xFA)                { return 0; }
    else if (key_code == PS2_BREAK_CODE) { ps2_on_break = 1; }
    else if (key_code == PS2_ENTER)      { return 0x0A; }
    else if (key_code == PS2_ESC)        { return 0x1B; }
    else if (key_code == PS2_BACKSPACE)  { return 0x5C; }
    else if (key_code == PS2_SPACE)      { return 0x20; }
    else if (key_code == PS2_TAB)        { return 0x09; }
    else if (key_code == 0x1C)           { return 0x61; } // 'a'
    else if (key_code == 0x32)           { return 0x62; } // 'b'
    else if (key_code == 0x21)           { return 0x63; } // 'c'
    else if (key_code == 0x23)           { return 0x64; } // 'd'
    else if (key_code == 0x24)           { return 0x65; } // 'e'
    else if (key_code == 0x2B)           { return 0x66; } // 'f'
    else if (key_code == 0x34)           { return 0x67; } // 'g'
    else if (key_code == 0x33)           { return 0x68; } // 'h'
    else if (key_code == 0x43)           { return 0x69; } // 'i'
    else if (key_code == 0x3B)           { return 0x6A; } // 'j'
    else if (key_code == 0x42)           { return 0x6B; } // 'k'
    else if (key_code == 0x4B)           { return 0x6C; } // 'l'
    else if (key_code == 0x3A)           { return 0x6D; } // 'm'
    else if (key_code == 0x31)           { return 0x6E; } // 'n'
    else if (key_code == 0x44)           { return 0x6F; } // 'o'
    else if (key_code == 0x4D)           { return 0x70; } // 'p'
    else if (key_code == 0x15)           { return 0x71; } // 'q'
    else if (key_code == 0x2D)           { return 0x72; } // 'r'
    else if (key_code == 0x1B)           { return 0x73; } // 's'
    else if (key_code == 0x2C)           { return 0x74; } // 't'
    else if (key_code == 0x3C)           { return 0x75; } // 'u'
    else if (key_code == 0x2A)           { return 0x76; } // 'v'
    else if (key_code == 0x1D)           { return 0x77; } // 'w'
    else if (key_code == 0x22)           { return 0x78; } // 'x'
    else if (key_code == 0x35)           { return 0x79; } // 'y'
    else if (key_code == 0x1A)           { return 0x7A; } // 'z'
    else if (key_code == 0x45)           { return 0x30; } // '0'
    else if (key_code == 0x16)           { return 0x31; } // '1'
    else if (key_code == 0x1E)           { return 0x32; } // '2'
    else if (key_code == 0x26)           { return 0x33; } // '3'
    else if (key_code == 0x25)           { return 0x34; } // '4'
    else if (key_code == 0x2E)           { return 0x35; } // '5'
    else if (key_code == 0x36)           { return 0x36; } // '6'
    else if (key_code == 0x3D)           { return 0x37; } // '7'
    else if (key_code == 0x3E)           { return 0x38; } // '8'
    else if (key_code == 0x46)           { return 0x39; } // '9'
    else if (key_code == 0x49)           { return 0x2E; } // '.'
    else if (key_code == 0x4A)           { return 0x2F; } // '/'
    else if (key_code == 0x0E)           { return 0x60; } // '`'
    else if (key_code == 0x4E)           { return 0x2D; } // '-'
    else if (key_code == 0x55)           { return 0x3D; } // '='

    return 0;
}


void do_tests()
{
    int str1[6];
    str1[0] = 0x48;
    str1[1] = 0x45;
    str1[2] = 0x4C;
    str1[3] = 0x4C;
    str1[4] = 0x4F;
    str1[5] = 0x00;

    int msg[7];
    msg[0] = 0x46;
    msg[1] = 0x41;
    msg[2] = 0x49;
    msg[3] = 0x4c;
    msg[4] = 0x45;
    msg[5] = 0x44;
    msg[6] = 0x00;

    cnt = 0;
    cnt = cnt + test_eq(strcmp(&str1, &str1), 0);
    cnt = cnt + test_neq(strcmp(&str1, &msg), 0);

    if (cnt != 0) {
        lcd_clear();
        print_str(&msg);

        halt;
    }
}


int getchar()
{
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


// Main
void main()
{
    int msg[6];
    msg[0] = 0x53;
    msg[1] = 0x54;
    msg[2] = 0x41;
    msg[3] = 0x52;
    msg[4] = 0x54;
    msg[5] = 0x00;

    lcd_clear();
    print_str(&msg);

    led_set(0x55, 0xFF);

    while (1) {
        input_char = getchar();
        lcd_put_char(input_char);
    }
}

do_tests();
main();
halt;
