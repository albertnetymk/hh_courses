
Print
method decl: ComputeFac
If
Assign
Assign
@.formatting.string = private constant [4 x i8] c"%d\0A\00"
%Fac = type {}
define i32 @main() {
entry:
  %retval = alloca i32
  store i32 0, i32 * %retval
  %tmp0 = call i8 * @malloc(i32 0)
  %tmp1 = bitcast i8 * %tmp0 to %Fac *
  %tmp2 = call i32 @Fac.ComputeFac(%Fac * %tmp1, i32 4)
  %tmp3 = getelementptr [4 x i8] * @.formatting.string, i32 0, i32 0
  %tmp4 = call i32 @printf(i8 * %tmp3, i32 %tmp2)
  %0 = load i32 * %retval
  ret i32 %0
}
define i32 @Fac.ComputeFac(%Fac * %object_para, i32 %num_para) {
entry:
  %retval = alloca i32
  %num_addr = alloca i32
  store i32 %num_para, i32 * %num_addr
  %num_aux_addr = alloca i32
  %tmp5 = load i32 * %num_addr
  %tmp6 = icmp slt i32 %tmp5, 1
  br i1 %tmp6, label %then0, label %else0
then0:
  store i32 1, i32 * %num_aux_addr
  br label %end0
else0:
  %tmp7 = load i32 * %num_addr
  %tmp8 = load i32 * %num_addr
  %tmp9 = sub i32 %tmp8, 1
  %tmp10 = call i32 @Fac.ComputeFac(%Fac * %object_para, i32 %tmp9)
  %tmp11 = mul i32 %tmp7, %tmp10
  store i32 %tmp11, i32 * %num_aux_addr
  br label %end0
end0:
  %tmp12 = load i32 * %num_aux_addr
  store i32 %tmp12, i32 * %retval
  %0 = load i32 * %retval
  ret i32 %0
}
declare i32 @printf (i32)
declare i8 * @malloc (i32)
