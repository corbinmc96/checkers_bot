����   3 �
 5 q	 4 r	 4 s	 4 t	 4 u	 4 v
  w x
  y
  z {	 4 |
 4 }
 4 ~
 4  �
  �
 4 � �
  � �
 � �
 � �
 � �
  �
 � �Ac�    	 � �
  �
 � �
 � �
 � �
  �
  �
  �
  �
 4 �
  �
  �
  �
  �	 4 � �
 , q
 4 �
  �
 , �
 , �
 , � � � � myBoard LBoard; 	gameRobot LRobot; isOnZeroSide Z color Ljava/lang/String; xo valueFactor I opponent LPlayer; <init> (Ljava/lang/String;ZLRobot;)V Code LineNumberTable (Ljava/lang/String;ZI)V (Z)V getValueFactor ()I getIsOnZeroSide ()Z getRobot 	()LRobot; getColor ()Ljava/lang/String; getPlayerPieces (LBoard;)[LPiece; StackMapTable � � � getXO setOpponent (LPlayer;)V calculateBestMove 	(I)LMove; rankBestMoves 
(I)[LMove; � � � minimax (LBoard;IZ)D performMove (LMove;LBoard;)V setBoard 
(LBoard;)V getBoard 	()LBoard; getAllMoves (LBoard;)[LMove; � x takeTurn 	(LGame;)V 
SourceFile Player.java C � < = : ; 8 9 > = ? @ � � Piece � � � �   A B \ ] g h i j Board C � a b java/lang/Double � � Move � � � � � � � � � � � � � � � � � � � � � � � � � � � � � � � � � K L � � � H � R � � 6 7 java/util/ArrayList Q R � j � � � J � � [LMove; Player java/lang/Object [LPiece; [LBoard; [D [Ljava/lang/Double; ()V totalPiecesLeft (LPlayer;)I getPiecesOnBoard 
()[LPiece; 	getPlayer 
()LPlayer; (LBoard;LMove;)V valueOf (D)Ljava/lang/Double; java/util/Collections reverseOrder ()Ljava/util/Comparator; java/util/Arrays sort ,([Ljava/lang/Object;Ljava/util/Comparator;)V ([Ljava/lang/Object;)V doubleValue ()D ArraysHelper find ([DD)I java/lang/System out Ljava/io/PrintStream; getWaypoints ()[[I deepToString '([Ljava/lang/Object;)Ljava/lang/String; java/io/PrintStream println (Ljava/lang/String;)V calculateValue getMovePiece 	()LPiece; getDestination ()[I setLocation ([I)V getLocation 	setIsKing calculatePiecesToJump removePiece 
(LPiece;)V getMovesOfPiece add (Ljava/lang/Object;)Z size toArray (([Ljava/lang/Object;)[Ljava/lang/Object;! 4 5     6 7    8 9    : ;    < =    > =    ? @    A B     C D  E   <     *� *+� *� *-� �    F          	       C G  E   <     *� *+� *� *� �    F          	    !  "  C H  E   *     
*� *� �    F       $  % 	 &  I J  E        *� �    F       )  K L  E        *� �    F       -  M N  E        *� �    F       1  O P  E        *� �    F       5  Q R  E   �     @+*� � M>+� 	:�66� !2:� 
*� ,�S����,�    F       9 	 :  ; ' < 0 = 8 ; > @ S    �   T U V V  �   W P  E   8     *� � �*� �    F       D  E 
 G S    
  X Y  E   "     *+� �    F   
    K  L  Z [  E         *� 2�    F       O  \ ]  E  {  	   �**� � M,�� N,��:6,�� +-� Y*� ,2� S*-2� R����,�� :6,�� 1� S����,�� :*� � � � � � 6,�� 92� � 6 R,2S� ,2� � �  ���Ʋ � !�    F   ^    S 	 T  U  V  W 2 X A V G [ N \ X ] e \ k a r b z c � f � i � l � m � n � o � i � r � s S   2 �   T 3 ^ _  � .� 	 `� �  3� � <  a b  E       �� +� "9�� *� +� :� 
*+� :�� :��:6�� 5� Y+2� S*2d� � � R����� *� �h�9� *� �h�9:		�6
6
� Y	19*� � %� �� 89� 1�� )9� "� �� 9� �� 9�����    F   v    w  x 
 z  ~    � % � - � 4 � ? � Q � k � q � u � � � � � � � � � � � � � � � � � � � � � � � � � � � � � � � S   y �  3�  ^ _� .  T U 3 ^ _  _ T U�    T U 3 ^ _  _ T U� 	� 
�  _� (� �  	 c d  E   �     j*� #*� $� %*� #� 
� &� *� #� '.� "*� #� (� *� #� '.� *� #� (*+� )M,�>6� ,2:+� *����    F   * 
   �  �  � & � 1 � = � E � ] � c � i � S    1�  V�   e f  E   "     *+� +�    F   
    �  �  g h  E        *� +�    F       �  i j  E   �     d� ,Y� -M*+� .N-�66� :-2:+� /:�66		� 	2:
,
� 0W�	������,,� 1� � 2� 3N-�    F   "    �  � " � @ � G � M � S � b � S   L �   T U k V  �  
 T U k V l 3  �   T U k V  �  m n    o    p