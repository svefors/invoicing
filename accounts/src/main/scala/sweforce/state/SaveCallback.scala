package sweforce.state

/**
 * Created with IntelliJ IDEA.
 * User: sveffa
 * Date: 10/3/12
 * Time: 12:10 PM
 * To change this template use File | Settings | File Templates.
 */
trait SaveCallback[T] {

  def save(valueToSave: T)

}
