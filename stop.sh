#!/bin/bash
pid=`ps aux | grep arrosage | awk '{print $2}'`
kill -9 $pid
