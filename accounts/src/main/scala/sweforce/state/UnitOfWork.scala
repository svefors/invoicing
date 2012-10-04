package sweforce.state

/**
 * Created with IntelliJ IDEA.
 * User: sveffa
 * Date: 10/3/12
 * Time: 11:13 AM
 * To change this template use File | Settings | File Templates.
 */
trait UnitOfWork {

//  var editors = Set[StateEditor[_]]()
//
//  var callbacks = Map[StateEditor[_], SaveCallback[StateEditor[_]]]()

//  def registerStateEditor(editor: StateEditor[_], saveCallback: SaveCallback[StateEditor[_]]) {
//    editors = editors + editor
//    callbacks = callbacks + (editor -> saveCallback)
//  }

  def commit() /*= {
    editors.foreach(st => {
      callbacks(st).save(st)
    })
  }
*/
//  private def clean() {
//    editors = Set[StateEditor[_]]()
//
//    callbacks = Map[StateEditor[_], SaveCallback[StateEditor[_]]]()
//
//  }
//
  def rollback() /*= {
    clean()
  }                */

}
