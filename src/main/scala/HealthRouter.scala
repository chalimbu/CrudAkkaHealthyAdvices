//for make the routing
import akka.http.scaladsl.server.directives.PathDirectives.path
import akka.http.scaladsl.server.{Directives, Route}
//for json support
import de.heikoseeberger.akkahttpcirce.FailFastCirceSupport._
import io.circe.generic.auto._

class HealthRouter(healthAdviceRepository: HealthAdviceRepository) extends Directives {
  def route: Route= pathPrefix("advice"){
    concat(
      pathEndOrSingleSlash{
          get {
            complete(healthAdviceRepository.getOneRandomAdvice)
          }
      },
      path(Segment){ categoryLoking=>
        concat(
          pathEnd{
            get{//categoryLoking
              complete(healthAdviceRepository.getCategory(categoryLoking))
            }
          }
        )
      },
      path("alimentation"){
        concat(
          pathEnd{
            get{
              complete(healthAdviceRepository.getCategory("Alimentation"))
            }
          }
        )
      }
    )
  }
}
