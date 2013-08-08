package grails_2_3_playground



import grails.transaction.*
import static org.springframework.http.HttpStatus.*

@Transactional(readOnly = true)
class ProtocolController {

    static allowedMethods = [save: "POST", update: "PUT", delete: "DELETE"]

    def index(Integer max) {
        params.max = Math.min(max ?: 50, 100)
        respond Protocol.list(params), model: [protocolInstanceCount: Protocol.count()]
    }

    def show(Protocol protocolInstance) {
        respond protocolInstance
    }

    def create() {
        respond new Protocol(params)
    }

    @Transactional
    def save(Protocol protocolInstance) {
        if (protocolInstance.hasErrors()) {
            respond protocolInstance.errors, view: 'create'
        } else {
            protocolInstance.save flush: true
            request.withFormat {
                form {
                    flash.message = message(code: 'default.created.message', args: [message(code: 'protocolInstance.label', default: 'Protocol'), protocolInstance.id])
                    redirect protocolInstance
                }
                '*' { respond protocolInstance, [status: CREATED] }
            }
        }
    }

    def edit(Protocol protocolInstance) {
        respond protocolInstance
    }

    @Transactional
    def update(Protocol protocolInstance) {
        if (protocolInstance == null) {
            render status: 404
        } else if (protocolInstance.hasErrors()) {
            respond protocolInstance.errors, view: 'edit'
        } else {
            protocolInstance.save flush: true
            request.withFormat {
                form {
                    flash.message = message(code: 'default.updated.message', args: [message(code: 'Protocol.label', default: 'Protocol'), protocolInstance.id])
                    redirect protocolInstance
                }
                '*' { respond protocolInstance, [status: OK] }
            }
        }
    }

    @Transactional
    def delete(Protocol protocolInstance) {
        if (protocolInstance) {
            protocolInstance.delete flush: true
            request.withFormat {
                form {
                    flash.message = message(code: 'default.deleted.message', args: [message(code: 'Protocol.label', default: 'Protocol'), protocolInstance.id])
                    redirect action: "index", method: "GET"
                }
                '*' { render status: NO_CONTENT }
            }
        } else {
            render status: NOT_FOUND
        }
    }
}

