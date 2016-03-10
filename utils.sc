LEDG = 0xA003;  // LED


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


int strlen(str_ptr)
{
    int cnt;

    cnt = 0;
    while (*str_ptr != 0x00) {
        cnt++;
        str_ptr++;
    }

    return cnt;
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
