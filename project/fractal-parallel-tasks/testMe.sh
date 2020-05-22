#!/bin/bash

function testWithMultipleRuns() {    
    for thread in {1..5}
    do
        for run in {1..5}
        do
            echo "the stuff"
        done
    done
}


function testWithSingleRun() {
    maxThreads=20
    imageSize="640x480"
    
    date >> out.txt
    echo "${maxThreads}" >> out.txt
    echo "${imageSize}" >> out.txt

    for thread in $(seq "${maxThreads}")
    do
        options="-t ${thread} -s ${imageSize}"
        
        bash ./runMe.sh "${options}" >> out.txt
        wait $!

        echo "" >> out.txt
    done
}

# clear the file first
truncate -s 0 out.txt

testWithSingleRun

