section .text
global primo

primo:
	push rbp
	mov rbp, rsp
    
	; Argumento: EDI = número a verificar (entero de 32 bits)
	mov eax, edi
    
	; Casos especiales (n <= 1 → falso; n == 2 → verdadero)
	cmp eax, 1
	jle not_prime
	cmp eax, 2
	je prime
    
	; Calcular raíz cuadrada del número
	cvtsi2sd xmm0, eax      ; Convertir entero a double
	sqrtsd xmm1, xmm0       ; xmm1 = sqrt(n)
	cvttsd2si ecx, xmm1     ; ECX = floor(sqrt(n)) (límite)
    
	; Bucle de verificación (divisores de 2 a sqrt(n))
	mov ebx, 2              ; i = 2

loop_start:
	cmp ebx, ecx
	jg prime                ; Si i > límite → es primo
	mov eax, edi            ; Recargar número original
	xor edx, edx            ; Limpiar EDX para división
	div ebx                 ; EDX = n % i
	test edx, edx           ; Verificar si resto == 0
	jz not_prime            ; Si es divisible → no es primo
	inc ebx                 ; i++
	jmp loop_start

prime:
	mov eax, 1              ; Devolver true (1)
	jmp fin

not_prime:
	xor eax, eax            ; Devolver false (0)

fin:
	pop rbp
	ret