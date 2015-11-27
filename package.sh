#!/bin/bash
# make report PDF
wkhtmltopdf report.htm report.pdf
# put relevant files including tests
zip wfjv99_huffman.zip -r lib/ src/ test/ report.pdf encoder.jar decoder.jar run_tests.sh