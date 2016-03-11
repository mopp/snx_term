SEED        = 10;

DAIKICHI[0] = 0x44;
DAIKICHI[1] = 0x41;
DAIKICHI[2] = 0x49;
DAIKICHI[3] = 0x4b;
DAIKICHI[4] = 0x49;
DAIKICHI[5] = 0x43;
DAIKICHI[6] = 0x48;
DAIKICHI[7] = 0x49;
DAIKICHI[8] = 0x00;

CHUKICHI[0] = 0x43;
CHUKICHI[1] = 0x48;
CHUKICHI[2] = 0x55;
CHUKICHI[3] = 0x4b;
CHUKICHI[4] = 0x49;
CHUKICHI[5] = 0x43;
CHUKICHI[6] = 0x48;
CHUKICHI[7] = 0x49;
CHUKICHI[8] = 0x00;

SHOKICHI[0] = 0x53;
SHOKICHI[1] = 0x48;
SHOKICHI[2] = 0x4f;
SHOKICHI[3] = 0x4b;
SHOKICHI[4] = 0x49;
SHOKICHI[5] = 0x43;
SHOKICHI[6] = 0x48;
SHOKICHI[7] = 0x49;
SHOKICHI[8] = 0x00;

rand_seed = SEED;

int rand3(){
    rand_seed = 0x00FF & xor(rand_seed, (rand_seed>>1));
    rand_seed = 0x00FF & xor(rand_seed, (rand_seed<<1));
    rand_seed = 0x00FF & xor(rand_seed, (rand_seed>>2));
    return modulo(rand_seed, 3);
}

int exe_omikuji(){
    int omikuji_result;
    omikuji_result = rand3();

    if(omikuji_result == 0){
        change_color(0xD000);
        println_str(&DAIKICHI);
    }
    else if(omikuji_result == 1){
        change_color(0xE000);
        println_str(&CHUKICHI);
    }
    else if(omikuji_result == 2){
        change_color(0xB000);
        println_str(&SHOKICHI);
    }

    reverse_color();
}