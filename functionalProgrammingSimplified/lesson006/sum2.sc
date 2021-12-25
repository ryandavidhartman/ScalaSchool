val sum2: List[Int] => Int = (ints: List[Int]) => ints match {
	case Nil => 0
	case x :: tail => x + sum2(tail)
}

// Java Disassembled Byte Code

/*
public class $line4.$read$$iw implements java.io.Serializable {
  public final $line4.$read $outer;

  public scala.Function1<scala.collection.immutable.List<java.lang.Object>, java.lang.Object> sum2();
    Code:
       0: aload_0
       1: getfield      #24                 // Field sum2:Lscala/Function1;
       4: areturn

  public $line4.$read $line4$$read$$iw$$$outer();
    Code:
       0: aload_0
       1: getfield      #30                 // Field $outer:L$line4/$read;
       4: areturn

  public static final int $anonfun$sum2$1($line4.$read$$iw, scala.collection.immutable.List);
    Code:
       0: aload_1
       1: astore_3
       2: getstatic     #40                 // Field scala/collection/immutable/Nil$.MODULE$:Lscala/collection/immutable/Nil$;
       5: aload_3
       6: invokevirtual #44                 // Method java/lang/Object.equals:(Ljava/lang/Object;)Z
       9: ifeq          17
      12: iconst_0
      13: istore_2
      14: goto          83
      17: goto          20
      20: aload_3
      21: instanceof    #46                 // class scala/collection/immutable/$colon$colon
      24: ifeq          71
      27: aload_3
      28: checkcast     #46                 // class scala/collection/immutable/$colon$colon
      31: astore        4
      33: aload         4
      35: invokevirtual #50                 // Method scala/collection/immutable/$colon$colon.head:()Ljava/lang/Object;
      38: invokestatic  #56                 // Method scala/runtime/BoxesRunTime.unboxToInt:(Ljava/lang/Object;)I
      41: istore        5
      43: aload         4
      45: invokevirtual #60                 // Method scala/collection/immutable/$colon$colon.next$access$1:()Lscala/collection/immutable/List;
      48: astore        6
      50: iload         5
      52: aload_0
      53: invokevirtual #62                 // Method sum2:()Lscala/Function1;
      56: aload         6
      58: invokeinterface #68,  2           // InterfaceMethod scala/Function1.apply:(Ljava/lang/Object;)Ljava/lang/Object;
      63: invokestatic  #56                 // Method scala/runtime/BoxesRunTime.unboxToInt:(Ljava/lang/Object;)I
      66: iadd
      67: istore_2
      68: goto          83
      71: goto          74
      74: new           #70                 // class scala/MatchError
      77: dup
      78: aload_3
      79: invokespecial #74                 // Method scala/MatchError."<init>":(Ljava/lang/Object;)V
      82: athrow
      83: iload_2
      84: ireturn

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
      12: invokespecial #84                 // Method java/lang/Object."<init>":()V
      15: aload_0
      16: aload_0
      17: invokedynamic #104,  0            // InvokeDynamic #0:apply:(L$line4/$read$$iw;)Lscala/Function1;
      22: putfield      #24                 // Field sum2:Lscala/Function1;
      25: return

  public static final java.lang.Object $anonfun$sum2$1$adapted($line4.$read$$iw, scala.collection.immutable.List);
    Code:
       0: aload_0
       1: aload_1
       2: invokestatic  #106                // Method $anonfun$sum2$1:(L$line4/$read$$iw;Lscala/collection/immutable/List;)I
       5: invokestatic  #110                // Method scala/runtime/BoxesRunTime.boxToInteger:(I)Ljava/lang/Integer;
       8: areturn
}
*/


