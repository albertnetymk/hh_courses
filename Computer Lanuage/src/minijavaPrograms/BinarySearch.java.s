@.formatting.string = private constant [4 x i8] c"%d\0A\00"
%BS = type {{i32, [0 x i32]},i32}
define i32 @main() {
entry:
  %retval = alloca i32
  store i32 0, i32 * %retval
  %tmp0 = call i8 * @malloc(i32 8)
  %tmp1 = bitcast i8 * %tmp0 to %BS *
  %tmp2 = call i32 @BS.Start(%BS * %tmp1, i32 20)
  %tmp3 = getelementptr [4 x i8] * @.formatting.string, i32 0, i32 0
  %tmp4 = call i32 @printf(i8 * %tmp3, i32 %tmp2)
  %0 = load i32 * %retval
  ret i32 %0
}
define i32 @BS.Start(%BS * %object_para, i32 %sz_para) {
entry:
  %sz_addr = alloca i32
  store i32 %sz_para, i32 * %sz_addr
  %aux01_addr = alloca i32
  %aux02_addr = alloca i32
  %tmp5 = load i32 * %sz_addr
  %tmp6 = call i32 @BS.Init(%BS * %object_para, i32 %tmp5)
  store i32 %tmp6, i32 * %aux01_addr
  %tmp7 = call i32 @BS.Print(%BS * %object_para)
  store i32 %tmp7, i32 * %aux02_addr
  %tmp8 = call i1 @BS.Search(%BS * %object_para, i32 8)
  br i1 %tmp8, label %then0, label %else0
then0:
  %tmp9 = getelementptr [4 x i8] * @.formatting.string, i32 0, i32 0
  %tmp10 = call i32 @printf(i8 * %tmp9, i32 1)
  br label %end0
else0:
  %tmp11 = getelementptr [4 x i8] * @.formatting.string, i32 0, i32 0
  %tmp12 = call i32 @printf(i8 * %tmp11, i32 0)
  br label %end0
end0:
  %tmp13 = call i1 @BS.Search(%BS * %object_para, i32 19)
  br i1 %tmp13, label %then1, label %else1
then1:
  %tmp14 = getelementptr [4 x i8] * @.formatting.string, i32 0, i32 0
  %tmp15 = call i32 @printf(i8 * %tmp14, i32 1)
  br label %end1
else1:
  %tmp16 = getelementptr [4 x i8] * @.formatting.string, i32 0, i32 0
  %tmp17 = call i32 @printf(i8 * %tmp16, i32 0)
  br label %end1
end1:
  %tmp18 = call i1 @BS.Search(%BS * %object_para, i32 20)
  br i1 %tmp18, label %then2, label %else2
then2:
  %tmp19 = getelementptr [4 x i8] * @.formatting.string, i32 0, i32 0
  %tmp20 = call i32 @printf(i8 * %tmp19, i32 1)
  br label %end2
else2:
  %tmp21 = getelementptr [4 x i8] * @.formatting.string, i32 0, i32 0
  %tmp22 = call i32 @printf(i8 * %tmp21, i32 0)
  br label %end2
end2:
  %tmp23 = call i1 @BS.Search(%BS * %object_para, i32 21)
  br i1 %tmp23, label %then3, label %else3
then3:
  %tmp24 = getelementptr [4 x i8] * @.formatting.string, i32 0, i32 0
  %tmp25 = call i32 @printf(i8 * %tmp24, i32 1)
  br label %end3
else3:
  %tmp26 = getelementptr [4 x i8] * @.formatting.string, i32 0, i32 0
  %tmp27 = call i32 @printf(i8 * %tmp26, i32 0)
  br label %end3
end3:
  %tmp28 = call i1 @BS.Search(%BS * %object_para, i32 37)
  br i1 %tmp28, label %then4, label %else4
then4:
  %tmp29 = getelementptr [4 x i8] * @.formatting.string, i32 0, i32 0
  %tmp30 = call i32 @printf(i8 * %tmp29, i32 1)
  br label %end4
else4:
  %tmp31 = getelementptr [4 x i8] * @.formatting.string, i32 0, i32 0
  %tmp32 = call i32 @printf(i8 * %tmp31, i32 0)
  br label %end4
end4:
  %tmp33 = call i1 @BS.Search(%BS * %object_para, i32 38)
  br i1 %tmp33, label %then5, label %else5
then5:
  %tmp34 = getelementptr [4 x i8] * @.formatting.string, i32 0, i32 0
  %tmp35 = call i32 @printf(i8 * %tmp34, i32 1)
  br label %end5
else5:
  %tmp36 = getelementptr [4 x i8] * @.formatting.string, i32 0, i32 0
  %tmp37 = call i32 @printf(i8 * %tmp36, i32 0)
  br label %end5
end5:
  %tmp38 = call i1 @BS.Search(%BS * %object_para, i32 39)
  br i1 %tmp38, label %then6, label %else6
then6:
  %tmp39 = getelementptr [4 x i8] * @.formatting.string, i32 0, i32 0
  %tmp40 = call i32 @printf(i8 * %tmp39, i32 1)
  br label %end6
else6:
  %tmp41 = getelementptr [4 x i8] * @.formatting.string, i32 0, i32 0
  %tmp42 = call i32 @printf(i8 * %tmp41, i32 0)
  br label %end6
end6:
  %tmp43 = call i1 @BS.Search(%BS * %object_para, i32 50)
  br i1 %tmp43, label %then7, label %else7
then7:
  %tmp44 = getelementptr [4 x i8] * @.formatting.string, i32 0, i32 0
  %tmp45 = call i32 @printf(i8 * %tmp44, i32 1)
  br label %end7
else7:
  %tmp46 = getelementptr [4 x i8] * @.formatting.string, i32 0, i32 0
  %tmp47 = call i32 @printf(i8 * %tmp46, i32 0)
  br label %end7
end7:
  ret i32 999
}
define i1 @BS.Search(%BS * %object_para, i32 %num_para) {
entry:
  %num_addr = alloca i32
  store i32 %num_para, i32 * %num_addr
  %bs01_addr = alloca i1
  %right_addr = alloca i32
  %left_addr = alloca i32
  %var_cont_addr = alloca i1
  %medium_addr = alloca i32
  %aux01_addr = alloca i32
  %nt_addr = alloca i32
  store i32 0, i32 * %aux01_addr
  store i1 0, i1 * %bs01_addr
  %tmp49 = getelementptr %BS * %object_para, i32 0, i32 0
  %tmp50 = bitcast {i32, [0 x i32]} * %tmp49 to {i32, [0 x i32]} * *
  %tmp48 = load {i32, [0 x i32]} * * %tmp50
  %tmp52 = getelementptr {i32, [0 x i32]} * %tmp48, i32 0, i32 0
  %tmp51 = load i32 * %tmp52
  store i32 %tmp51, i32 * %right_addr
  %tmp53 = load i32 * %right_addr
  %tmp54 = sub i32 %tmp53, 1
  store i32 %tmp54, i32 * %right_addr
  store i32 0, i32 * %left_addr
  store i1 1, i1 * %var_cont_addr
  br label %cond8
cond8:
  %tmp55 = load i1 * %var_cont_addr
  br i1 %tmp55, label %then8, label %end8
then8:
  %tmp56 = load i32 * %left_addr
  %tmp57 = load i32 * %right_addr
  %tmp58 = add i32 %tmp56, %tmp57
  store i32 %tmp58, i32 * %medium_addr
  %tmp59 = load i32 * %medium_addr
  %tmp60 = call i32 @BS.Div(%BS * %object_para, i32 %tmp59)
  store i32 %tmp60, i32 * %medium_addr
  %tmp62 = getelementptr %BS * %object_para, i32 0, i32 0
  %tmp63 = bitcast {i32, [0 x i32]} * %tmp62 to {i32, [0 x i32]} * *
  %tmp61 = load {i32, [0 x i32]} * * %tmp63
  %tmp64 = load i32 * %medium_addr
  %tmp66 = getelementptr {i32, [0 x i32]} * %tmp61, i32 0, i32 1, i32 %tmp64
  %tmp65 = load i32 * %tmp66
  store i32 %tmp65, i32 * %aux01_addr
  %tmp67 = load i32 * %num_addr
  %tmp68 = load i32 * %aux01_addr
  %tmp69 = icmp slt i32 %tmp67, %tmp68
  br i1 %tmp69, label %then9, label %else9
then9:
  %tmp70 = load i32 * %medium_addr
  %tmp71 = sub i32 %tmp70, 1
  store i32 %tmp71, i32 * %right_addr
  br label %end9
else9:
  %tmp72 = load i32 * %medium_addr
  %tmp73 = add i32 %tmp72, 1
  store i32 %tmp73, i32 * %left_addr
  br label %end9
end9:
  %tmp74 = load i32 * %aux01_addr
  %tmp75 = load i32 * %num_addr
  %tmp76 = call i1 @BS.Compare(%BS * %object_para, i32 %tmp74, i32 %tmp75)
  br i1 %tmp76, label %then10, label %else10
then10:
  store i1 0, i1 * %var_cont_addr
  br label %end10
else10:
  store i1 1, i1 * %var_cont_addr
  br label %end10
end10:
  %tmp77 = load i32 * %right_addr
  %tmp78 = load i32 * %left_addr
  %tmp79 = icmp slt i32 %tmp77, %tmp78
  br i1 %tmp79, label %then11, label %else11
then11:
  store i1 0, i1 * %var_cont_addr
  br label %end11
else11:
  store i32 0, i32 * %nt_addr
  br label %end11
end11:
  br label %cond8
end8:
  %tmp80 = load i32 * %aux01_addr
  %tmp81 = load i32 * %num_addr
  %tmp82 = call i1 @BS.Compare(%BS * %object_para, i32 %tmp80, i32 %tmp81)
  br i1 %tmp82, label %then12, label %else12
then12:
  store i1 1, i1 * %bs01_addr
  br label %end12
else12:
  store i1 0, i1 * %bs01_addr
  br label %end12
end12:
  %tmp83 = load i1 * %bs01_addr
  ret i1 %tmp83
}
define i32 @BS.Div(%BS * %object_para, i32 %num_para) {
entry:
  %num_addr = alloca i32
  store i32 %num_para, i32 * %num_addr
  %count01_addr = alloca i32
  %count02_addr = alloca i32
  %aux03_addr = alloca i32
  store i32 0, i32 * %count01_addr
  store i32 0, i32 * %count02_addr
  %tmp84 = load i32 * %num_addr
  %tmp85 = sub i32 %tmp84, 1
  store i32 %tmp85, i32 * %aux03_addr
  br label %cond13
cond13:
  %tmp86 = load i32 * %count02_addr
  %tmp87 = load i32 * %aux03_addr
  %tmp88 = icmp slt i32 %tmp86, %tmp87
  br i1 %tmp88, label %then13, label %end13
then13:
  %tmp89 = load i32 * %count01_addr
  %tmp90 = add i32 %tmp89, 1
  store i32 %tmp90, i32 * %count01_addr
  %tmp91 = load i32 * %count02_addr
  %tmp92 = add i32 %tmp91, 2
  store i32 %tmp92, i32 * %count02_addr
  br label %cond13
end13:
  %tmp93 = load i32 * %count01_addr
  ret i32 %tmp93
}
define i1 @BS.Compare(%BS * %object_para, i32 %num1_para, i32 %num2_para) {
entry:
  %num1_addr = alloca i32
  store i32 %num1_para, i32 * %num1_addr
  %num2_addr = alloca i32
  store i32 %num2_para, i32 * %num2_addr
  %retval_addr = alloca i1
  %aux02_addr = alloca i32
  store i1 0, i1 * %retval_addr
  %tmp94 = load i32 * %num2_addr
  %tmp95 = add i32 %tmp94, 1
  store i32 %tmp95, i32 * %aux02_addr
  %tmp96 = load i32 * %num1_addr
  %tmp97 = load i32 * %num2_addr
  %tmp98 = icmp slt i32 %tmp96, %tmp97
  br i1 %tmp98, label %then14, label %else14
then14:
  store i1 0, i1 * %retval_addr
  br label %end14
else14:
  %tmp99 = load i32 * %num1_addr
  %tmp100 = load i32 * %aux02_addr
  %tmp101 = icmp slt i32 %tmp99, %tmp100
  %tmp103 = alloca i1
  br i1 %tmp101, label %then15, label %else15
then15:
  store i1 0, i1 * %tmp103
  br label %end15
else15:
  store i1 1, i1 * %tmp103
  br label %end15
end15:
  %tmp102 = load i1 * %tmp103
  br i1 %tmp102, label %then16, label %else16
then16:
  store i1 0, i1 * %retval_addr
  br label %end16
else16:
  store i1 1, i1 * %retval_addr
  br label %end16
end16:
  br label %end14
end14:
  %tmp104 = load i1 * %retval_addr
  ret i1 %tmp104
}
define i32 @BS.Print(%BS * %object_para) {
entry:
  %j_addr = alloca i32
  store i32 1, i32 * %j_addr
  br label %cond17
cond17:
  %tmp105 = load i32 * %j_addr
  %tmp107 = getelementptr %BS * %object_para, i32 0, i32 1
  %tmp106 = load i32 * %tmp107
  %tmp108 = icmp slt i32 %tmp105, %tmp106
  br i1 %tmp108, label %then17, label %end17
then17:
  %tmp110 = getelementptr %BS * %object_para, i32 0, i32 0
  %tmp111 = bitcast {i32, [0 x i32]} * %tmp110 to {i32, [0 x i32]} * *
  %tmp109 = load {i32, [0 x i32]} * * %tmp111
  %tmp112 = load i32 * %j_addr
  %tmp114 = getelementptr {i32, [0 x i32]} * %tmp109, i32 0, i32 1, i32 %tmp112
  %tmp113 = load i32 * %tmp114
  %tmp115 = getelementptr [4 x i8] * @.formatting.string, i32 0, i32 0
  %tmp116 = call i32 @printf(i8 * %tmp115, i32 %tmp113)
  %tmp117 = load i32 * %j_addr
  %tmp118 = add i32 %tmp117, 1
  store i32 %tmp118, i32 * %j_addr
  br label %cond17
end17:
  %tmp119 = getelementptr [4 x i8] * @.formatting.string, i32 0, i32 0
  %tmp120 = call i32 @printf(i8 * %tmp119, i32 99999)
  ret i32 0
}
define i32 @BS.Init(%BS * %object_para, i32 %sz_para) {
entry:
  %sz_addr = alloca i32
  store i32 %sz_para, i32 * %sz_addr
  %j_addr = alloca i32
  %k_addr = alloca i32
  %aux02_addr = alloca i32
  %aux01_addr = alloca i32
  %tmp121 = load i32 * %sz_addr
  %tmp122 = getelementptr %BS * %object_para, i32 0, i32 1
  store i32 %tmp121, i32 * %tmp122
  %tmp123 = load i32 * %sz_addr
  %tmp124 = mul i32 %tmp123, 4
  %tmp125 = add i32 %tmp124, 4
  %tmp126 = call i8 * @malloc(i32 %tmp125)
  %tmp127 = bitcast i8 * %tmp126 to {i32, [0 x i32]} *
  %tmp128 = getelementptr {i32, [0 x i32]} * %tmp127, i32 0, i32 0
  store i32 %tmp123, i32 * %tmp128
  %tmp130 = getelementptr %BS * %object_para, i32 0, i32 0
  %tmp129 = bitcast {i32, [0 x i32]} * %tmp130 to {i32, [0 x i32]} * *
  store {i32, [0 x i32]} * %tmp127, {i32, [0 x i32]} * * %tmp129
  store i32 1, i32 * %j_addr
  %tmp132 = getelementptr %BS * %object_para, i32 0, i32 1
  %tmp131 = load i32 * %tmp132
  %tmp133 = add i32 %tmp131, 1
  store i32 %tmp133, i32 * %k_addr
  br label %cond18
cond18:
  %tmp134 = load i32 * %j_addr
  %tmp136 = getelementptr %BS * %object_para, i32 0, i32 1
  %tmp135 = load i32 * %tmp136
  %tmp137 = icmp slt i32 %tmp134, %tmp135
  br i1 %tmp137, label %then18, label %end18
then18:
  %tmp138 = load i32 * %j_addr
  %tmp139 = mul i32 2, %tmp138
  store i32 %tmp139, i32 * %aux01_addr
  %tmp140 = load i32 * %k_addr
  %tmp141 = sub i32 %tmp140, 3
  store i32 %tmp141, i32 * %aux02_addr
  %tmp143 = getelementptr %BS * %object_para, i32 0, i32 0
  %tmp142 = bitcast {i32, [0 x i32]} * %tmp143 to {i32, [0 x i32]} * *
  %tmp145 = load i32 * %j_addr
  %tmp146 = load i32 * %aux01_addr
  %tmp147 = load i32 * %aux02_addr
  %tmp148 = add i32 %tmp146, %tmp147
  %tmp144 = load {i32, [0 x i32]} * * %tmp142
  %tmp149 = getelementptr {i32, [0 x i32]} * %tmp144, i32 0, i32 1, i32 %tmp145
  store i32 %tmp148, i32 * %tmp149
  %tmp150 = load i32 * %j_addr
  %tmp151 = add i32 %tmp150, 1
  store i32 %tmp151, i32 * %j_addr
  %tmp152 = load i32 * %k_addr
  %tmp153 = sub i32 %tmp152, 1
  store i32 %tmp153, i32 * %k_addr
  br label %cond18
end18:
  ret i32 0
}
declare i32 @printf (i32)
declare i8 * @malloc (i32)
