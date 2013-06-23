#!/bin/bash

contains_files()
{
    for file in "$@"
    do
        if ! [ -f $file ]
        then
            return 1
        fi
    done
    return 0
}
