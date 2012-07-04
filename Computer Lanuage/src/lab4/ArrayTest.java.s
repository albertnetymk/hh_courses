@.formatting.string = private constant [4 x i8] c"%d\0A\00"
%A = type {{i32, [0 x i32]},i32}
define i32 @main() {
entry:
  %retval = alloca i32
  store i32 0, i32 * %retval
  %tmp0 = call i8 * @malloc(i32 8)
  %tmp1 = bitcast i8 * %tmp0 to %A *
  %tmp2 = call i32 @A.sum(%A * %tmp1, i32 100)
  %tmp3 = getelementptr [4 x i8] * @.formatting.string, i32 0, i32 0
  %tmp4 = call i32 @printf(i8 * %tmp3, i32 %tmp2)
  %0 = load i32 * %retval
  ret i32 %0
}
define i32 @A.sum(%A * %object_para, i32 %size_para) {
entry:
  %size_addr = alloca i32
  store i32 %size_para, i32 * %size_addr
  %i_addr = alloca i32
  %y_addr = alloca i32
  store i32 0, i32 * %i_addr
  %tmp5 = load i32 * %size_addr
  %tmp6 = getelementptr %A * %object_para, i32 0, i32 1
  store i32 %tmp5, i32 * %tmp6
  %tmp7 = load i32 * %size_addr
  %tmp8 = mul i32 %tmp7, 4
  %tmp9 = add i32 %tmp8, 4
  %tmp10 = call i8 * @malloc(i32 %tmp9)
  %tmp11 = bitcast i8 * %tmp10 to {i32, [0 x i32]} *
  %tmp12 = getelementptr {i32, [0 x i32]} * %tmp11, i32 0, i32 0
  store i32 %tmp7, i32 * %tmp12
  %tmp14 = getelementptr %A * %object_para, i32 0, i32 0
  %tmp13 = bitcast {i32, [0 x i32]} * %tmp14 to {i32, [0 x i32]} * *
  store {i32, [0 x i32]} * %tmp11, {i32, [0 x i32]} * * %tmp13
  br label %cond0
cond0:
  %tmp15 = load i32 * %i_addr
  %tmp16 = load i32 * %size_addr
  %tmp17 = icmp slt i32 %tmp15, %tmp16
  br i1 %tmp17, label %then0, label %end0
then0:
  %tmp19 = getelementptr %A * %object_para, i32 0, i32 0
  %tmp18 = bitcast {i32, [0 x i32]} * %tmp19 to {i32, [0 x i32]} * *
  %tmp21 = load i32 * %i_addr
  %tmp22 = load i32 * %i_addr
  %tmp20 = load {i32, [0 x i32]} * * %tmp18
  %tmp23 = getelementptr {i32, [0 x i32]} * %tmp20, i32 0, i32 1, i32 %tmp21
  store i32 %tmp22, i32 * %tmp23
  %tmp24 = load i32 * %i_addr
  %tmp25 = add i32 %tmp24, 1
  store i32 %tmp25, i32 * %i_addr
  br label %cond0
end0:
  store i32 0, i32 * %y_addr
  store i32 0, i32 * %i_addr
  br label %cond1
cond1:
  %tmp26 = load i32 * %i_addr
  %tmp28 = getelementptr %A * %object_para, i32 0, i32 1
  %tmp27 = load i32 * %tmp28
  %tmp29 = icmp slt i32 %tmp26, %tmp27
  br i1 %tmp29, label %then1, label %end1
then1:
  %tmp30 = load i32 * %y_addr
  %tmp32 = getelementptr %A * %object_para, i32 0, i32 0
  %tmp33 = bitcast {i32, [0 x i32]} * %tmp32 to {i32, [0 x i32]} * *
  %tmp31 = load {i32, [0 x i32]} * * %tmp33
  %tmp34 = load i32 * %i_addr
  %tmp36 = getelementptr {i32, [0 x i32]} * %tmp31, i32 0, i32 1, i32 %tmp34
  %tmp35 = load i32 * %tmp36
  %tmp37 = add i32 %tmp30, %tmp35
  store i32 %tmp37, i32 * %y_addr
  %tmp38 = load i32 * %i_addr
  %tmp39 = add i32 %tmp38, 1
  store i32 %tmp39, i32 * %i_addr
  br label %cond1
end1:
  %tmp40 = load i32 * %y_addr
  ret i32 %tmp40
}
declare i32 @printf (i32)
declare i8 * @malloc (i32)
