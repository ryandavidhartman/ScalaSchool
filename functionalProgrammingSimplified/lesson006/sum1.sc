val sum1: List[Int] => Int = (ints: List[Int]) => {
	var sum = 0
	for( i <- ints) {
		sum += i
	}
	sum
}

// Java Disassembled Byte Code
/*
public class $line4.$read$$iw implements java.io.Serializable {
  public final $line4.$read $outer;

  public scala.Function1<scala.collection.immutable.List<java.lang.Object>, java.lang.Object> sum1();
    Code:
       0: aload_0
       1: getfield      #24                 // Field sum1:Lscala/Function1;
       4: areturn

  public $line4.$read $line4$$read$$iw$$$outer();
    Code:
       0: aload_0
       1: getfield      #30                 // Field $outer:L$line4/$read;
       4: areturn

  public static final void $anonfun$sum1$2(scala.runtime.IntRef, int);
    Code:
       0: aload_0
       1: aload_0
       2: getfield      #40                 // Field scala/runtime/IntRef.elem:I
       5: iload_1
       6: iadd
       7: putfield      #40                 // Field scala/runtime/IntRef.elem:I
      10: return

  public static final int $anonfun$sum1$1(scala.collection.immutable.List);
    Code:
       0: iconst_0
       1: invokestatic  #48                 // Method scala/runtime/IntRef.create:(I)Lscala/runtime/IntRef;
       4: astore_1
       5: aload_0
       6: aload_1
       7: invokedynamic #65,  0             // InvokeDynamic #0:apply$mcVI$sp:(Lscala/runtime/IntRef;)Lscala/runtime/java8/JFunction1$mcVI$sp;
      12: invokevirtual #71                 // Method scala/collection/immutable/List.foreach:(Lscala/Function1;)V
      15: aload_1
      16: getfield      #40                 // Field scala/runtime/IntRef.elem:I
      19: ireturn

  public $line4.$read$$iw($line4.$read);
    Code:
       0: aload_1
       1: ifnonnull     6
       4: aconst_null
       5: athrow
       6: aload_0
       7: aload_1
       8: putfield      #30                 // Field $outer:L$line4/$read;
      11: aload_0
      12: invokespecial #78                 // Method java/lang/Object."<init>":()V
      15: aload_0
      16: invokedynamic #90,  0             // InvokeDynamic #1:apply:()Lscala/Function1;
      21: putfield      #24                 // Field sum1:Lscala/Function1;
      24: return

  public static final java.lang.Object $anonfun$sum1$1$adapted(scala.collection.immutable.List);
    Code:
       0: aload_0
       1: invokestatic  #92                 // Method $anonfun$sum1$1:(Lscala/collection/immutable/List;)I
       4: invokestatic  #98                 // Method scala/runtime/BoxesRunTime.boxToInteger:(I)Ljava/lang/Integer;

*/

