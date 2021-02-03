//
// Created by xjx on 2020-12-21.
//

#ifndef CANUTILS_H
#define CANUTILS_H

#ifdef __cplusplus
extern "C" {
#endif

int can_config(int bitrate, int loopback, int restart_ms);
int can_send(int id, int dlc, int extended,int rtr, int loopcount, int *data);
int can_dump_open(int id, int mask);
int can_dump_start(int s, struct can_frame *frame);
int can_dump_stop(int s);
int can_status();
#ifdef __cplusplus
}
#endif
#endif //CANUTILS_H
