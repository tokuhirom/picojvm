use strict;
use v5.10;
use Data::Dumper;

my @lines;
while (<DATA>) {
    chomp;
    my ($op, $code, $desc) = split /\t/, $_;
    push @lines, [$op, $code, $desc];
}
@lines = sort { $a->[1] <=> $b->[1] } @lines;

for (@lines) {
    my ($op, $code, $desc) = @$_;
    say qq!@{[ uc($op) ]}("$op", $code, "$desc"),!;
}

__DATA__
aaload	50	Load reference from array
aastore	83	Store into reference array
aconst_null	1	Push null
aload	25	Load reference from local variable
aload_0	42	Load reference from local variable 0
aload_1	43	Load reference from local variable 1
aload_2	44	Load reference from local variable 2
aload_3	45	Load reference from local variable 3
anewarray	189	Create new array of reference
areturn	176	Return reference from method
arraylength	190	Get length of array
astore	58	Store reference into local variable
astore_0	75	Store reference into local variable 0
astore_1	76	Store reference into local variable 1
astore_2	77	Store reference into local variable 2
astore_3	78	Store reference into local variable 3
athrow	191	Throw Throwable reference
baload	51	Load byte or boolean from array
bastore	84	Store into byte or boolean array
bipush	16	Push byte
breakpoint	202	Reserved for internal usage in debuggers
caload	52	Load char from array
castore	85	Store into char array
checkcast	192	Check whether reference is of given type
d2f	144	Convert double to float
d2i	142	Convert double to int
d2l	143	Convert double to long
dadd	99	Add double
daload	49	Load double from array
dastore	82	Store into double array
dcmpg	152	Compare double
dcmpl	151	Compare double
dconst_0	14	Push double value 0.0
dconst_1	15	Push double value 1.0
ddiv	111	Divide double
dload	24	Load double from local variable
dload_0	38	Load double from local variable 0
dload_1	39	Load double from local variable 1
dload_2	40	Load double from local variable 2
dload_3	41	Load double from local variable 3
dmul	107	Multiply double
dneg	119	Negate double
drem	115	Remainder double
dreturn	175	Return double from method
dstore	57	Store double into local variable
dstore_0	71	Store double into local variable 0
dstore_1	72	Store double into local variable 1
dstore_2	73	Store double into local variable 2
dstore_3	74	Store double into local variable 3
dsub	103	Subtract double
dup	89	Duplicate the top operand stack value
dup_x1	90	Duplicate the top operand stack value and insert two values down
dup_x2	91	Duplicate the top operand stack value and insert two or three values down
dup2	92	Duplicate the top one or two operand stack values
dup2_x1	93	Duplicate the top one or two operand stack values and insert two or three values down
dup2_x2	94	Duplicate the top one or two operand stack values and insert two, three, or four values down
f2d	141	Convert float to double
f2i	139	Convert float to int
f2l	140	Convert float to long
fadd	98	Add float
faload	48	Load float from array
fastore	81	Store into float array
fcmpg	150	Compare float
fcmpl	149	Compare float
fconst_0	11	Push float value 0.0
fconst_1	12	Push float value 1.0
fconst_2	13	Push float value 2.0
fdiv	110	Divide float
fload	23	Load float from local variable
fload_0	34	Load float from local variable 0
fload_1	35	Load float from local variable 1
fload_2	36	Load float from local variable 2
fload_3	37	Load float from local variable 3
fmul	106	Multiply float
fneg	118	Negate float
frem	114	Remainder float
freturn	174	Return float from method
fstore	56	Store float into local variable
fstore_0	67	Store float into local variable 0
fstore_1	68	Store float into local variable 1
fstore_2	69	Store float into local variable 2
fstore_3	70	Store float into local variable 3
fsub	102	Subtract float
getfield	180	Fetch field from object
getstatic	178	Get static field from class
goto	167	Unconditional jump
goto_w	200	Unconditional jump (wide index)
i2b	145	Convert int to byte
i2c	146	Convert int to char
i2d	135	Convert int to double
i2f	134	Convert int to float
i2l	133	Convert int to long
i2s	147	Convert int to short
iadd	96	Add int
iaload	46	Load int from array
iand	126	Boolean AND int
iastore	79	Store into int array
iconst_0	3	Push int constant 0
iconst_1	4	Push int constant 1
iconst_2	5	Push int constant 2
iconst_3	6	Push int constant 3
iconst_4	7	Push int constant 4
iconst_5	8	Push int constant 5
iconst_m1	2	Push int constant -1
idiv	108	Divide int
if_acmpeq	165	Jump if reference comparison succeeds
if_acmpne	166	Jump if reference comparison succeeds
if_icmpeq	159	Jump if int comparison succeeds
if_icmpge	162	Jump if int comparison succeeds
if_icmpgt	163	Jump if int comparison succeeds
if_icmple	164	Jump if int comparison succeeds
if_icmplt	161	Jump if int comparison succeeds
if_icmpne	160	Jump if int comparison succeeds
ifeq	153	Jump if int comparison with zero succeeds
ifge	156	Jump if int comparison with zero succeeds
ifgt	157	Jump if int comparison with zero succeeds
ifle	158	Jump if int comparison with zero succeeds
iflt	155	Jump if int comparison with zero succeeds
ifne	154	Jump if int comparison with zero succeeds
ifnonnull	199	Jump if reference not null
ifnull	198	Jump if reference is null
iinc	132	Increment local variable by constant
iload	21	Load int from local variable
iload_0	26	Load int from local variable
iload_1	27	Load int from local variable
iload_2	28	Load int from local variable
iload_3	29	Load int from local variable
impdep1	254	Reserved for internal usage in JVM
impdep2	255	Reserved for internal usage in JVM
imul	104	Multiply int
ineg	116	Negate int
instanceof	193	Determine if reference is of given type
invokedynamic	186	Invoke a dynamically-computed call site
invokeinterface	185	Invoke interface method
invokespecial	183	Directly invoke instance (initialization) method of the current class or its supertypes
invokestatic	184	Invoke static method
invokevirtual	182	Invoke instance method, dispatch based on class
ior	128	Boolean OR int
irem	112	Remainder int
ireturn	172	Return int from method
ishl	120	Shift left int
ishr	122	Arithmetic shift right int
istore	54	Store int into local variable
istore_0	59	Store int into local variable 0
istore_1	60	Store int into local variable 1
istore_2	61	Store int into local variable 2
istore_3	62	Store int into local variable 3
isub	100	Subtract int
iushr	124	Logical shift right int
ixor	130	Boolean XOR int
jsr	168	Jump subroutine
jsr_w	201	Jump subroutine (wide index)
l2d	138	Convert long to double
l2f	137	Convert long to float
l2i	136	Convert long to int
ladd	97	Add long
laload	47	Load long from array
land	127	Boolean AND long
lastore	80	Store into long array
lcmp	148	Compare long
lconst_0	9	Push long constant
lconst_1	10	Push long constant
ldc	18	Push item from constant pool
ldc_w	19	Push item from constant pool (wide index)
ldc2_w	20	Push long or double from constant pool (wide index)
ldiv	109	Divide long
lload	22	Load long from local variable
lload_0	30	Load long from local variable 0
lload_1	31	Load long from local variable 1
lload_2	32	Load long from local variable 2
lload_3	33	Load long from local variable 3
lmul	105	Multiply long
lneg	117	Negate long
lookupswitch	171	Access jump table by key match and jump
lor	129	Boolean OR long
lrem	113	Remainder long
lreturn	173	Return long from method
lshl	121	Shift left long
lshr	123	Arithmetic shift right long
lstore	55	Store long into local variable
lstore_0	63	Store long into local variable 0
lstore_1	64	Store long into local variable 1
lstore_2	65	Store long into local variable 2
lstore_3	66	Store long into local variable 3
lsub	101	Subtract long
lushr	125	Logical shift right long
lxor	131	Boolean XOR long
monitorenter	194	Enter monitor for object
monitorexit	195	Exit monitor for object
multianewarray	197	Create new multidimensional array
new	187	Create new object
newarray	188	Create new array
nop	0	Do nothing
pop	87	Pop the top operand stack value
pop2	88	Pop the top one or two operand stack values
putfield	181	Set field in object
putstatic	179	Set static field in class
ret	169	Return from subroutine
return	177	Return void from method
saload	53	Load short from array
sastore	86	Store into short array
sipush	17	Push short
swap	95	Swap the top two operand stack values
tableswitch	170	Access jump table by index and jump
wide	196	Extend local variable index by additional bytes
