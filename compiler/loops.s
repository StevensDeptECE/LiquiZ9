@
@ loop examples
@

    .global f1
f1:

1:
    sub r0, #1
    cmp r0, #1
    bne 1b
    bx  lr

    .global f2
f2:
    mov r1, #0
1:
    add r1, #1
    cmp r1, r0
    bne 1b
    bx  lr

