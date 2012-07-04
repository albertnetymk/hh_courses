; ModuleID = 'test0.c'
target datalayout = "e-p:32:32:32-i1:8:8-i8:8:8-i16:16:16-i32:32:32-i64:32:64-f32:32:32-f64:32:64-v64:64:64-v128:128:128-a0:0:64-f80:32:32-n8:16:32"
target triple = "i386-linux-gnu"

%struct.anon = type { i32*, i32 }
%struct.test = type { %struct.anon }

define i32 @main() nounwind {
entry:
  %retval = alloca i32                            ; <i32*> [#uses=2]
  %0 = alloca i32                                 ; <i32*> [#uses=2]
  %1 = alloca %struct.anon                        ; <%struct.anon*> [#uses=4]
  %x = alloca %struct.anon*                       ; <%struct.anon**> [#uses=4]
  %y = alloca %struct.test*                       ; <%struct.test**> [#uses=2]
  %"alloca point" = bitcast i32 0 to i32          ; <i32> [#uses=0]
  %2 = call noalias i8* @malloc(i32 8) nounwind   ; <i8*> [#uses=1]
  %3 = bitcast i8* %2 to %struct.anon*            ; <%struct.anon*> [#uses=1]
  store %struct.anon* %3, %struct.anon** %x, align 4
  %4 = load %struct.anon** %x, align 4            ; <%struct.anon*> [#uses=1]
  %5 = getelementptr inbounds %struct.anon* %4, i32 0, i32 0 ; <i32**> [#uses=1]
  store i32* null, i32** %5, align 4
  %6 = load %struct.anon** %x, align 4            ; <%struct.anon*> [#uses=1]
  %7 = getelementptr inbounds %struct.anon* %6, i32 0, i32 1 ; <i32*> [#uses=1]
  store i32 0, i32* %7, align 4
  %8 = call noalias i8* @malloc(i32 8) nounwind   ; <i8*> [#uses=1]
  %9 = bitcast i8* %8 to %struct.test*            ; <%struct.test*> [#uses=1]
  store %struct.test* %9, %struct.test** %y, align 4
  %10 = load %struct.anon** %x, align 4           ; <%struct.anon*> [#uses=2]
  %11 = getelementptr inbounds %struct.anon* %1, i32 0, i32 0 ; <i32**> [#uses=1]
  %12 = getelementptr inbounds %struct.anon* %10, i32 0, i32 0 ; <i32**> [#uses=1]
  %13 = load i32** %12, align 4                   ; <i32*> [#uses=1]
  store i32* %13, i32** %11, align 4
  %14 = getelementptr inbounds %struct.anon* %1, i32 0, i32 1 ; <i32*> [#uses=1]
  %15 = getelementptr inbounds %struct.anon* %10, i32 0, i32 1 ; <i32*> [#uses=1]
  %16 = load i32* %15, align 4                    ; <i32> [#uses=1]
  store i32 %16, i32* %14, align 4
  %17 = load %struct.test** %y, align 4           ; <%struct.test*> [#uses=1]
  %18 = getelementptr inbounds %struct.test* %17, i32 0, i32 0 ; <%struct.anon*> [#uses=2]
  %19 = getelementptr inbounds %struct.anon* %18, i32 0, i32 0 ; <i32**> [#uses=1]
  %20 = getelementptr inbounds %struct.anon* %1, i32 0, i32 0 ; <i32**> [#uses=1]
  %21 = load i32** %20, align 4                   ; <i32*> [#uses=1]
  store i32* %21, i32** %19, align 4
  %22 = getelementptr inbounds %struct.anon* %18, i32 0, i32 1 ; <i32*> [#uses=1]
  %23 = getelementptr inbounds %struct.anon* %1, i32 0, i32 1 ; <i32*> [#uses=1]
  %24 = load i32* %23, align 4                    ; <i32> [#uses=1]
  store i32 %24, i32* %22, align 4
  store i32 0, i32* %0, align 4
  %25 = load i32* %0, align 4                     ; <i32> [#uses=1]
  store i32 %25, i32* %retval, align 4
  br label %return

return:                                           ; preds = %entry
  %retval1 = load i32* %retval                    ; <i32> [#uses=1]
  ret i32 %retval1
}

declare noalias i8* @malloc(i32) nounwind
