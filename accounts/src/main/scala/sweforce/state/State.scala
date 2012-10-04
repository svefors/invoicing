package sweforce.state

/**
 * Created with IntelliJ IDEA.
 * User: sveffa
 * Date: 10/3/12
 * Time: 6:26 AM
 * To change this template use File | Settings | File Templates.
 */
trait State[T <: State[T]] {

  self: T =>

  def applyChange(change : Any) : T

  def applyChanges(changes : Seq[Any]) : T ={
    var t = this
    changes.foreach(change =>{
      t = t.applyChange(change)
    })
    return t
  }

}
