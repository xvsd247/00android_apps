//
// Created by xjx on 2020-12-21.
//

#ifndef CANUTILS_H
#define CANUTILS_H

#ifdef __cplusplus
extern "C" {
#endif

int can_config(int bitrate, int loopback, int restart_ms);
int can_send(int id, int dlc, int extended,int rtr, int infinite, int loopcount, int *data);
int can_dump(int family, int type, int proto, int filter_count, int *filter_p, char * data, int *callback(int id, int r, int idc));


#ifdef __cplusplus
}
#endif
#endif //CANUTILS_H
