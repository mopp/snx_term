int TEST_STR1[6];
int TEST_STR2[5];
int TEST_STR_MSG[7];


TEST_STR1[0] = 0x48;
TEST_STR1[1] = 0x45;
TEST_STR1[2] = 0x4C;
TEST_STR1[3] = 0x4C;
TEST_STR1[4] = 0x4F;
TEST_STR1[5] = 0x00;

TEST_STR2[0] = 0x31;
TEST_STR2[1] = 0x32;
TEST_STR2[2] = 0x33;
TEST_STR2[3] = 0x00;

TEST_STR_MSG[0] = 0x46;
TEST_STR_MSG[1] = 0x41;
TEST_STR_MSG[2] = 0x49;
TEST_STR_MSG[3] = 0x4c;
TEST_STR_MSG[4] = 0x45;
TEST_STR_MSG[5] = 0x44;
TEST_STR_MSG[6] = 0x00;

void do_tests()
{
    int cnt;

    cnt = 0;
    cnt = cnt + test_eq(strcmp(&TEST_STR1, &TEST_STR1), 0);
    cnt = cnt + test_neq(strcmp(&TEST_STR1, &TEST_STR_MSG), 0);
    cnt = cnt + test_eq(strncmp(&TEST_STR1, &TEST_STR1, 1), 0);
    cnt = cnt + test_eq(strncmp(&TEST_STR1, &TEST_STR1, 2), 0);
    cnt = cnt + test_neq(strncmp(&TEST_STR1, &TEST_STR_MSG, 2), 0);
    cnt = cnt + test_eq(strlen(&TEST_STR1), 5);
    cnt = cnt + test_eq(strlen(&START_MSG), 5);
    cnt = cnt + test_eq(atoi(&TEST_STR2), 123);
    cnt = cnt + test_eq(is_number_str(&TEST_STR2), 1);
    cnt = cnt + test_eq(is_number_str(&TEST_STR_MSG), 0);
    cnt = cnt + test_eq(strchr(&TEST_STR2, 0x31), (&TEST_STR2));
    cnt = cnt + test_eq(strchr(&TEST_STR2, 0x32), (&TEST_STR2 + 1));
    cnt = cnt + test_eq(modulo(10, 2), 0);
    cnt = cnt + test_eq(modulo(10, 3), 1);
    cnt = cnt + test_eq(modulo(200, 7), 4);
    cnt = cnt + test_eq(div(4, 4), 1);
    cnt = cnt + test_eq(div(12, 4), 3);
    cnt = cnt + test_eq(div(45, 5), 9);

    if (cnt != 0) {
        clear();
        print_str(&TEST_STR_MSG);

        halt;
    }
}

do_tests();
