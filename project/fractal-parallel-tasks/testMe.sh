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
    for thread in {1..5}
    do
        options="-t ${thread} -s 1280x960"
        
        bash ./runMe.sh "${options}" >> out.txt
        wait $!

        echo "" >> out.txt
    done
}

# clear the file first
truncate -s 0 out.txt

testWithSingleRun

