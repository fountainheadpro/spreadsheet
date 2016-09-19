import scala.util.Random

def compress(s: String)={
  var counter = 0
  var prevSymbol = s(0)
  var currentPos = 0
  var res=Seq[String]()
  while(currentPos<s.length){
    if (prevSymbol == s(currentPos)) {
      counter+=1
    } else{
      res :+= s"$counter$prevSymbol"
      counter=1
    }
    prevSymbol=s(currentPos)
    currentPos+=1
  }
  res.mkString
}

compress("1122344")

/*
def merge(large: Array[Int], lei: Int, small: Array[Int]): Array[Int]={
  require(small.length == large.length - lei-1)
  var li=lei
  var si = small.length-1
  var curr = large.length-1
  while(li >=0 && si >=0){
    large(curr)=small(si) max large(li)
    if(large(curr) == small(si)) si-=1 else li-=1
    curr-=1
  }
  while(si>=0){
    large(curr)=small(si)
    curr-=1
    si-=1
  }
  large
}


var large= Array(2, 3, 5, 0, 0, 0, 0, 0, 0, 0)
var small=Array(0,1,4,5,6,13,15)
merge(large, 2, small)

*/




/*
def binarySearch(el: Int, input: Array[Int]) ={

  var s=0
  var e=input.length-1
  var mid=0
  var found = false
  while (e>=s && !found) {
    mid = (e + s) / 2
    if (input(mid) > el) {
      e = mid-1
    } else if (input(mid) < el) {
      s = mid+1
    } else {
      found = true
    }
    println(s"$s, $e")
  }
  if (found) Some(mid) else None
}


binarySearch(15, Array(-1, -2, 0, 2, 5, 7, 8, 9,11, 32))


def partition(input: Array[Int], s: Int, e: Int)={




  require(e<input.length)
  require(e>=s)
  require(s>=0)
  val m=(e+s)/2
  val p=input(m)
  //swap(m,e)
  var si=s; var ei=e//-1
  while(si<ei){
    if(input(si)<p) si+=1
    else if(input(ei)>=p) ei-=1
    else swap(si,ei)
  }
  //swap(e,ei)
  ei
}



def partition(input: Array[Int], s: Int, e: Int) = {
  def swap(i: Int, j: Int) {
    val temp = input(i)
    input(i) = input(j)
    input(j) = temp
  }

  val random = new Random()
  val pivot = random.nextInt(e - s + 1) + s
  swap(pivot, e)
  println(input.mkString(","))
  var small = s - 1
  for(i <- s to e) {
    println(s"$small -> $i")
    if(input(i) < input(e)) {
      small+=1
      if(i != small) {
        swap(small, i)
        println(input.mkString(","))
      }
    }
  }
  small+=1
  if(small != e) swap(small, e)
  small
}

val input = Array(32, -1, 0, 5, -2, 0, 2, -5, 7, -8, 9, -11, 3)
val index=partition(input, 0, input.length-1)
input
input(index)


*/
