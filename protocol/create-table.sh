#!/bin/bash

# creates a table with the bytes as the header and the ASCII mappings below

input_string="$*"

# Split input string into an array of bytes
IFS=' ' read -ra bytes <<< "$input_string"

# Print the header for the Markdown table
header=""
underline=""
for byte in "${bytes[@]}"; do
    header+="| $byte "
    underline+="|----"
done
header+="|"
underline+="|"

echo "$header"
echo "$underline"

# Print ASCII representation, excluding non-printable characters
ascii_row=""
for byte in "${bytes[@]}"; do
    if [[ $byte =~ ^[0-9A-Fa-f]{2}$ ]]; then
        decimal_value=$((16#$byte))
        if (( decimal_value >= 33 && decimal_value <= 126 )); then
            ascii_char=$(printf "\\x$byte")
            ascii_row+="|  $ascii_char "
        else
            ascii_row+="|    "
        fi
    else
        ascii_row+="|  ?  "
    fi
done
echo "$ascii_row|"
