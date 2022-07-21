import caliban._
import sttp.tapir.json.circe._
import zio.{Runtime, ULayer, ZIO, ZLayer}

class Api {
  val catRepoLayer: ULayer[CatRepo] = ZLayer.succeed(new CatRepo())
  implicit val runtime: Runtime[CatRepo] = zio.Runtime.unsafeFromLayer(catRepoLayer)
  val i = runtime.unsafeRun(createInterpreter)

  def route = {
    AkkaHttpAdapter.makeHttpService(i)
  }

  def createInterpreter: ZIO[CatRepo, CalibanError.ValidationError, GraphQLInterpreter[CatRepo, CalibanError]] = {
    import ApiSchema._
    GraphQL.graphQL[CatRepo, Queries, Mutations, Unit](
      RootResolver(
        Queries(
          () => CatRepo.getAllCats
        ),
        Mutations(
          CatRepo.addCat
        )
      )
    ).interpreter
  }

}

object Api {

}
