LEDG   = 0xA003; // LED

int xor(xor_p, xor_q)
{
    return (xor_p | xor_q) & ~(xor_p & xor_q);
}


int led_set(led_pattern, led_mask)
{
    current_bits = *LEDG;
    changed_bits = ((current_bits & ~led_mask) | (led_pattern & led_mask));
    *LEDG        = changed_bits;

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
