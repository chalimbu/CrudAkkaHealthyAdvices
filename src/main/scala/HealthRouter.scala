//for make the routing
import akka.http.scaladsl.server.{Directives,Route}
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
      path("exercise"){
        concat(
          pathEnd{
            get{
              complete(healthAdviceRepository.getCategory("Exercise"))
            }
          }
        )
      }
    )
  }
}
