#!/bin/bash

function testWithMultipleRuns() {    
    for thread in {1..3}
    do
        for run in {1..5}
        do
            bash ./run.sh -t 4 "${thread}" >> out.txt
            wait $!
        done
    done
}


function testWithSingleRun() {
    for thread in {1..5}
    do
        bash ./run.sh -t "${thread}" >> out.txt
        wait $!
    done
}

# clear the file first
truncate -s 0 out.txt

testWithSingleRun

