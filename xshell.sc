// Peripheral addr.
LCD = 0xA020;

// LCD
LCD_MAX_COLUMN = 16;
LCD_MAX_ROW    = 2;
lcd_current_x  = 0;
lcd_current_y  = 0;

// PS2
PS2DAT = 0xA004;
PS2IN  = 0xA005;

// LED
LEDG=0xA003;


void lcd_put_char(c)
{
    if (lcd_current_x == LCD_MAX_COLUMN) {
        lcd_current_x = 0;
        lcd_current_y = lcd_current_y + 1;
    }

    if (LCD_MAX_ROW <= lcd_current_y) {
        lcd_current_y = 0;
    }

    if (c == 0x0A) {
        lcd_current_y = lcd_current_y + 1;
        lcd_current_x = 0;
    } else {
        *(LCD + ((lcd_current_y * LCD_MAX_COLUMN) + lcd_current_x)) = c;
        lcd_current_x = lcd_current_x + 1;
    }
}


void lcd_clear()
{
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


int keymap[0x1A];
keymap[0x1C] = 0x61; // 'a'
keymap[0x32] = 0x62; // 'b'
keymap[0x21] = 0x63; // 'c'
keymap[0x23] = 0x64; // 'd'
keymap[0x24] = 0x65; // 'e'
keymap[0x2B] = 0x66; // 'f'
keymap[0x34] = 0x67; // 'g'
keymap[0x33] = 0x68; // 'h'
keymap[0x43] = 0x69; // 'i'
keymap[0x3B] = 0x6A; // 'j'
keymap[0x42] = 0x6B; // 'k'
keymap[0x4B] = 0x6C; // 'l'
keymap[0x3A] = 0x6D; // 'm'
keymap[0x31] = 0x6E; // 'n'
keymap[0x44] = 0x6F; // 'o'
keymap[0x4D] = 0x70; // 'p'
keymap[0x15] = 0x71; // 'q'
keymap[0x2D] = 0x72; // 'r'
keymap[0x1B] = 0x73; // 's'
keymap[0x2C] = 0x74; // 't'
keymap[0x3C] = 0x75; // 'u'
keymap[0x2A] = 0x76; // 'v'
keymap[0x1D] = 0x77; // 'w'
keymap[0x22] = 0x78; // 'x'
keymap[0x35] = 0x79; // 'y'
keymap[0x1A] = 0x7A; // 'z'

PS2_BREAK_CODE = 0xf0;
PS2_ENTER      = 0x5A;
PS2_ESC        = 0x76;
PS2_BACKSPACE  = 0x66;
PS2_SPACE      = 0x29;
PS2_TAB        = 0x0D;
PS2_CAPS       = 0x58;
PS2_L_SHIFT    = 0x12;
PS2_L_ALT      = 0x11;
PS2_L_CTRL     = 0x14;
PS2_R_SHIFT    = 0x59;
PS2_BREAK_CODE = 0xF0;
on_break       = 0;
int decode_key(key_code)
{
    if (key_code == 0xFA) {
        return 0;
    }

    if (on_break == 1) {
        on_break = 0;
        return 0;
    }

         if (key_code == PS2_BREAK_CODE) { on_break = 1; }
    else if (key_code == PS2_ENTER)      { return 0x0A; }
    // else if (key_code == PS2_ESC)     { return 0x00; }
    else if (key_code == PS2_BACKSPACE) { return 0x5C; }
    else if (key_code == PS2_SPACE)     { return 0x20; }
    else if (key_code == PS2_TAB)       { return 0x09; }
    else if (key_code == 0x1C)          { return 0x61; } // 'a'
    else if (key_code == 0x32)          { return 0x62; } // 'b'
    else if (key_code == 0x21)          { return 0x63; } // 'c'
    else if (key_code == 0x23)          { return 0x64; } // 'd'
    else if (key_code == 0x24)          { return 0x65; } // 'e'
    else if (key_code == 0x2B)          { return 0x66; } // 'f'
    else if (key_code == 0x34)          { return 0x67; } // 'g'
    else if (key_code == 0x33)          { return 0x68; } // 'h'
    else if (key_code == 0x43)          { return 0x69; } // 'i'
    else if (key_code == 0x3B)          { return 0x6A; } // 'j'
    else if (key_code == 0x42)          { return 0x6B; } // 'k'
    else if (key_code == 0x4B)          { return 0x6C; } // 'l'
    else if (key_code == 0x3A)          { return 0x6D; } // 'm'
    else if (key_code == 0x31)          { return 0x6E; } // 'n'
    else if (key_code == 0x44)          { return 0x6F; } // 'o'
    else if (key_code == 0x4D)          { return 0x70; } // 'p'
    else if (key_code == 0x15)          { return 0x71; } // 'q'
    else if (key_code == 0x2D)          { return 0x72; } // 'r'
    else if (key_code == 0x1B)          { return 0x73; } // 's'
    else if (key_code == 0x2C)          { return 0x74; } // 't'
    else if (key_code == 0x3C)          { return 0x75; } // 'u'
    else if (key_code == 0x2A)          { return 0x76; } // 'v'
    else if (key_code == 0x1D)          { return 0x77; } // 'w'
    else if (key_code == 0x22)          { return 0x78; } // 'x'
    else if (key_code == 0x35)          { return 0x79; } // 'y'
    else if (key_code == 0x1A)          { return 0x7A; } // 'z'

    return 0;
}


// Main
void main()
{
    int str[32];
    str[0]  = 0x48;
    str[1]  = 0x45;
    str[2]  = 0x4C;
    str[3]  = 0x4C;
    str[4]  = 0x4F;
    str[5]  = 0x0A;
    str[6]  = 0x57;
    str[7]  = 0x4F;
    str[8]  = 0x52;
    str[9]  = 0x4C;
    str[10] = 0x44;
    str[11] = 0x00;

    lcd_clear();
    print_str(&str);

    while (1) {
        if (*PS2IN) {
            raw_key_code = *PS2DAT;
            decoded_key_code = decode_key(raw_key_code);
            if (decoded_key_code != 0) {
                lcd_put_char(decoded_key_code);
            }
        }
        *LEDG = 1;
    }
}


main();
halt;
