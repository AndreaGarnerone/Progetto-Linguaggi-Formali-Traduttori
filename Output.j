.class public Output 
.super java/lang/Object

.method public <init>()V
 aload_0
 invokenonvirtual java/lang/Object/<init>()V
 return
.end method

.method public static print(I)V
 .limit stack 2
 getstatic java/lang/System/out Ljava/io/PrintStream;
 iload_0 
 invokestatic java/lang/Integer/toString(I)Ljava/lang/String;
 invokevirtual java/io/PrintStream/println(Ljava/lang/String;)V
 return
.end method

.method public static read()I
 .limit stack 3
 new java/util/Scanner
 dup
 getstatic java/lang/System/in Ljava/io/InputStream;
 invokespecial java/util/Scanner/<init>(Ljava/io/InputStream;)V
 invokevirtual java/util/Scanner/next()Ljava/lang/String;
 invokestatic java/lang/Integer.parseInt(Ljava/lang/String;)I
 ireturn
.end method

.method public static run()V
 .limit stack 1024
 .limit locals 256
 invokestatic Output/read()I
 invokestatic Output/read()I
 invokestatic Output/read()I
 istore 2
 istore 1
 istore 0
 goto L1
L1:
 ldc 1
 invokestatic Output/print(I)V
 iload 0
 iload 1
 imul 
 iload 2
 imul 
 invokestatic Output/print(I)V
 ldc 1
 invokestatic Output/print(I)V
 goto L2
L2:
L3:
 iload 0
 iload 1
 if_icmplt L4
 goto L5
L4:
 iload 0
 iload 2
 iadd 
 iload 1
 if_icmplt L6
 goto L7
L6:
 iload 1
 iload 2
 isub 
 istore 1
 goto L10
L10:
 iload 1
 invokestatic Output/print(I)V
 goto L11
L11:
 goto L9
L7:
 iload 0
 iload 2
 iadd 
 iload 1
 if_icmpeq L12
 goto L13
L12:
 ldc 0
 istore 1
 goto L14
L14:
 iload 1
 invokestatic Output/print(I)V
 goto L15
L15:
 goto L9
L13:
L8:
 iload 1
 ldc 1
 isub 
 istore 1
 goto L16
L16:
 iload 1
 invokestatic Output/print(I)V
 goto L17
L17:
L9:
 goto L3
L5:
 goto L18
L18:
 ldc 10
 dup
 istore 4
 istore 3
 goto L19
L19:
 iload 3
 invokestatic Output/print(I)V
 iload 4
 invokestatic Output/print(I)V
 goto L20
L20:
L0:
 return
.end method

.method public static main([Ljava/lang/String;)V
 invokestatic Output/run()V
 return
.end method

