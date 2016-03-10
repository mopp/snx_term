#include <stdio.h>
#include <string.h>


int main(int argc, char const* argv[])
{
    for (int i = 1; i < argc; i += 2) {
        char const* var_name = argv[i];
        char const* str      = argv[i + 1];
        size_t len           = strlen(str);

        printf("int %s[%zd];\n", var_name, len + 1);
        for (size_t j = 0; j < len; j++) {
            printf("    %s[%zd] = 0x%x;\n", var_name, j, str[j]);
        }
        printf("    %s[%zd] = 0x00;\n", var_name, len);
    }

    return 0;
}
