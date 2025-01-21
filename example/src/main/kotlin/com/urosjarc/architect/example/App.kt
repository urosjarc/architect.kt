import com.urosjarc.architect.example.interfaces.ChildRepo
import com.urosjarc.architect.example.interfaces.GrandParentRepo
import com.urosjarc.architect.example.interfaces.ParentRepo
import com.urosjarc.architect.example.services.SmsService
import com.urosjarc.architect.example.services.NetworkService
import com.urosjarc.architect.example.services.EmailService
import com.urosjarc.architect.example.usecases.RegisterNewChild
import com.urosjarc.architect.example.usecases.RegisterNewParent
public object App {
    //START MARK
    public object repos {
        public lateinit var childRepo: ChildRepo
        public lateinit var grandParentRepo: GrandParentRepo
        public lateinit var parentRepo: ParentRepo
    }
    public object services {
        public lateinit var smsService: SmsService
        public lateinit var networkService: NetworkService
        public lateinit var emailService: EmailService
    }
    public object usecases {
        public lateinit var registerNewChild: RegisterNewChild
        public lateinit var registerNewParent: RegisterNewParent
    }
    public fun check(){
        println("ChildRepo -> ${repos.childRepo}")
        println("GrandParentRepo -> ${repos.grandParentRepo}")
        println("ParentRepo -> ${repos.parentRepo}")
        println("SmsService -> ${services.smsService}")
        println("NetworkService -> ${services.networkService}")
        println("EmailService -> ${services.emailService}")
        println("RegisterNewChild -> ${usecases.registerNewChild}")
        println("RegisterNewParent -> ${usecases.registerNewParent}")
    }
    //END MARK
}