.code
calculate_checksum PROC
    xor eax, eax

checksum_loop:
    test rdx, rdx
    jz checksum_done

    movzx r8d, byte ptr [rcx]
    add eax, r8d
    inc rcx
    dec rdx
    jmp checksum_loop

checksum_done:
    and eax, 0FFh
    ret
calculate_checksum ENDP
END