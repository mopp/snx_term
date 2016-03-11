VGA     = 0xC000;
VGACLS  = 0x0FEC;
SCROLL  = 0x0FED;
CURSOR1 = 0x0FEE;  // 座標
CURSOR2 = 0x0FEF;  // 形状・色

COLOR_DEFAULT   = 0xF000;  // 文字:白　背景:黒
DISP_COLUMN_MAX = 80 - 1;
DISP_ROW_MAX    = 30 - 1;
VGA_MAX_ROW     = 32 - 1;  // 不可視領域含む
vga_current_x   = 0;
vga_current_y   = 0;
vga_current     = 0;
topline_y       = 0;
clear_cnt       = 0;
scroll_cnt      = 1;
scroll_flg      = 0;
color           = COLOR_DEFAULT;

void cursor_init()
{
    *(VGA | CURSOR1) = 0x0000;
    *(VGA | CURSOR2) = 0x03F0;
}

void change_color(select_color){
    color = select_color;
}

void reverse_color(){
    color = COLOR_DEFAULT;
}

void vga_clear()
{
    *(VGA | VGACLS) = color;
    while (*(VGA | VGACLS))
        ;

    vga_current_x   = 0;
    vga_current_y   = 0;
    vga_current     = 0;
    topline_y       = 0;
    clear_cnt       = 0;
    scroll_cnt      = 1;
    scroll_flg      = 0;
}


void vga_scroll()
{
    *(VGA | SCROLL) = scroll_cnt;

    if (scroll_cnt == VGA_MAX_ROW) {
        scroll_cnt = 0;
    } else {
        scroll_cnt++;
    }
}


void inc_adress_y()
{
    if (vga_current_y == VGA_MAX_ROW) {
        vga_current_y = 0;
    } else {
        vga_current_y++;
    }

    vga_current = (vga_current_y << 7) | vga_current_x;
}


void clear_topline()
{
    for (clear_cnt = 0; clear_cnt <= DISP_COLUMN_MAX; clear_cnt++) {
        topline_y = 0x1F & (vga_current_y + 3);
        topline = (topline_y << 7) | clear_cnt;
        *(VGA + topline) = 0xFF | color;
    }
}


void move_cursor()
{
    if (scroll_flg == 0) {
        *(VGA | CURSOR1) = vga_current;
    } else {
        *(VGA | CURSOR1) = 0x0E80 | vga_current_x;
    }
}


void line_break()
{
    if (vga_current_y == DISP_ROW_MAX) {
        scroll_flg = 1;
    }

    if (scroll_flg == 1) {
        clear_topline();  // 1番上の行をクリア
        vga_scroll();     // 1行スクロール
    }

    vga_current_x = 0;
    inc_adress_y();  // 1行下にずらす
    move_cursor();
}


void inc_adress_x()
{
    if (vga_current_x == DISP_COLUMN_MAX) {
        line_break();
    } else {
        vga_current_x++;
        vga_current = (vga_current_y << 7) | vga_current_x;
    }
}

void delete_char(){
    if(vga_current_x > 0){
        vga_current--;
        vga_current_x--;
        *(VGA + vga_current) = 0xFF | color;
        move_cursor();
    }
}

void vga_putchar(c)
{
    if (c == 0x0A) {
        line_break();
    }
    else if(c == 0x5C){
        delete_char();
    }
    else {
        *(VGA + vga_current) = c | color;
        inc_adress_x();  // 1文字分右にずらす
        move_cursor();
    }
}
