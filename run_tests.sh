#!/bin/sh
cd test/
for blocksize in 1 2 3; do
    for textfile in *.txt; do
        echo "Encoding $textfile with $blocksize-byte blocks..."
        java -jar ../encoder.jar $textfile $blocksize premade_results/$textfile.$blocksize.hc
    done
done

for hcfile in premade_results/*.hc; do
    echo "Decoding $hcfile..."
    java -jar ../decoder.jar $hcfile $hcfile.decoded.txt
done
cd ..