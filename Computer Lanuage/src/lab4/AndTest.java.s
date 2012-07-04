Print
method decl: f
If
Assign
Assign
@.formatting.string = private constant [4 x i8] c"%d\0A\00"
%A = type {i32}
define i32 @main() {
entry:
  %retval = alloca i32
  store i32 0, i32 * %retval
  %tmp0 = call i8 * @malloc(i32 4)
  %tmp1 = bitcast i8 * %tmp0 to %A *
  %tmp2 = call i32 @A.f(%A * %tmp1)
  %tmp3 = getelementptr [4 x i8] * @.formatting.string, i32 0, i32 0
  %tmp4 = call i32 @printf(i8 * %tmp3, i32 %tmp2)
  %0 = load i32 * %retval
  ret i32 %0
}
define i32 @A.f(%A * %object_para) {
entry:
  %tmp5 = icmp slt i32 3, 2
  %tmp7 = alloca i1
  br i1 %tmp5, label %then0, label %else0
then0:
  br i1 1, label %then1, label %else1
then1:
  store i1 %tmp5, i1 * %tmp7
  br label %end1
else1:
  store i1 1, i1 * %tmp7
  br label %end1
end1:
  br label %end0
else0:
  store i1 %tmp5, i1 * %tmp7
  br label %end0
end0:
  %tmp6 = load i1 * %tmp7
  br i1 %tmp6, label %then2, label %else2
then2:
  %tmp8 = getelementptr %A * %object_para, i32 0, i32 0
  store i32 2, i32 * %tmp8
  br label %end2
else2:
  %tmp9 = getelementptr %A * %object_para, i32 0, i32 0
  store i32 0, i32 * %tmp9
  br label %end2
end2:
  %tmp11 = getelementptr %A * %object_para, i32 0, i32 0
  %tmp10 = load i32 * %tmp11
  ret i32 %tmp10
}
declare i32 @printf (i32)
declare i8 * @malloc (i32)
