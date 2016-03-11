int test_eq(test_x, test_y)
{
    if (test_x == test_y) {
        return 0;
    }

    return 1;
}


int test_neq(test_n_x, test_n_y)
{
    if (test_eq(test_n_x, test_n_y) == 1) {
        return 0;
    }

    return 1;
}


int led_set(led_pattern, led_mask)
{
    int current_bits;
    int changed_bits;

    current_bits = *LEDG;
    changed_bits = ((current_bits & ~led_mask) | (led_pattern & led_mask));
    *LEDG = changed_bits;

    return changed_bits;
}


int strcmp(s1_ptr, s2_ptr)
{
    while (1) {
        if (*s1_ptr != *s2_ptr) {
            break;
        } else if (*s1_ptr == 0x00) {
            break;
        } else if (*s2_ptr == 0x00) {
            break;
        }

        s1_ptr++;
        s2_ptr++;
    }

    return *s1_ptr - *s2_ptr;
}


int strncmp(ncp1_ptr, ncp2_ptr, ncp_len)
{
    while (1) {
        if (*ncp1_ptr != *ncp2_ptr) {
            break;
        } else if (*ncp1_ptr == 0x00) {
            break;
        } else if (*ncp2_ptr == 0x00) {
            break;
        }

        ncp_len--;
        if (ncp_len == 0) {
            break;
        }

        ncp1_ptr++;
        ncp2_ptr++;
    }

    return *ncp1_ptr - *ncp2_ptr;
}


int strlen(ptr_strlen)
{
    int cnt;

    cnt = 0;
    while (*ptr_strlen != 0x00) {
        cnt++;
        ptr_strlen++;
    }

    return cnt;
}


int strchr(ptr_strchr, ch_strchr)
{
    while (*ptr_strchr != 0x00) {
        if (*ptr_strchr == ch_strchr) {
            return ptr_strchr;
        }

        ptr_strchr++;
    }

    return 0;
}


int is_number_str(ptr_numstr)
{
    while (*ptr_numstr != 0x00) {
        if ((*ptr_numstr < 0x30) || 0x39 < (*ptr_numstr)) {
            return 0;
        }
        ptr_numstr++;
    }

    return 1;
}


int atoi(ptr_atoi)
{
    int len;
    int base;
    int sum;
    int i;

    len  = strlen(ptr_atoi);
    base = 1;
    sum  = 0;

    for (i = len - 1; 0 <= i; i--) {
        sum = sum + (*(ptr_atoi + i) - 0x30) * base;
        base = base * 10;
    }

    return sum;
}


int xor(xor_p, xor_q)
{
    return (xor_p | xor_q) & ~(xor_p & xor_q);
}


int modulo(x_modul, y_modul)
{
    while (1) {
        if (x_modul < y_modul) {
            break;
        }
        x_modul = x_modul - y_modul;
    }

    return x_modul;
}


int div(n_div, d_div)
{
    int q_div;

    q_div = 0;
    while(d_div <= n_div) {
        q_div++;
        n_div = n_div - d_div;
    }

    return q_div;
}


int itoa(num_itoa, ptr_itoa)
{
    int i;
    int j;
    int len;
    int tmp;

    i = 0;
    while (1) {
        *(ptr_itoa + i) = modulo(num_itoa, 10) + 0x30;
        i++;
        num_itoa = div(num_itoa, 10);
        if (num_itoa == 0) {
            break;
        }
    }
    *(ptr_itoa + i) = 0x00;

    len = strlen(ptr_itoa);

    i = 0;
    j = len - 1;
    while (i < j) {
        tmp = *(ptr_itoa + i);
        *(ptr_itoa + i) = *(ptr_itoa + j);
        *(ptr_itoa + j) = tmp;
        i++;
        j--;
    }

    return ptr_itoa;
}


int trim_spaces(ptr_trim)
{
    int work_ptr;

    while (*ptr_trim != 0x00) {
        if (*ptr_trim == 0x20) {
            work_ptr = ptr_trim;
            while (*work_ptr != 0x00) {
                *work_ptr = *(work_ptr + 1);
                work_ptr++;
            }
        } else {
            ptr_trim++;
        }
    }

    return ptr_trim;
}
