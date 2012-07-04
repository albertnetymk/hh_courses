@.formatting.string = private constant [4 x i8] c"%d\0A\00"
%Test = type {}
define i32 @main() {
entry:
  %retval = alloca i32
  store i32 0, i32 * %retval
  %tmp0 = call i8 * @malloc(i32 0)
  %tmp1 = bitcast i8 * %tmp0 to %Test *
  %tmp2 = call i32 @Test.f(%Test * %tmp1)
  %tmp3 = getelementptr [4 x i8] * @.formatting.string, i32 0, i32 0
  %tmp4 = call i32 @printf(i8 * %tmp3, i32 %tmp2)
  %0 = load i32 * %retval
  ret i32 %0
}
define i32 @Test.f(%Test * %object_para) {
entry:
  %retval = alloca i32
  %a_addr = alloca i32
  store i32 0, i32 * %a_addr
  %tmp5 = load i32 * %a_addr
  %tmp6 = add i32 %tmp5, 2
  store i32 %tmp6, i32 * %a_addr
  %tmp7 = load i32 * %a_addr
  ret i32 %tmp7
}
declare i32 @printf (i32)
declare i8 * @malloc (i32)
