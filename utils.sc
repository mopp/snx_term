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
    while ((*s1_ptr == *s2_ptr) && (*s1_ptr != 0) && (*s2_ptr != 0)) {
        s1_ptr++;
        s2_ptr++;
    }

    return *s1_ptr - *s2_ptr;
}
