import caliban.schema.Schema

case class Cat(name: String)

object Cat {

  import ApiSchema._

  //  import caliban.schema.Schema._

  implicit val gqlSchema: Schema[Any, Cat] = obj[Any, Cat]("Cat")(
    implicit ft =>
      List(
        field("name")(_.name),
        field("speed")(_.name.length)
      )
  )

}