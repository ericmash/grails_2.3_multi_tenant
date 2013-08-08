package grails_2_3_playground

class Protocol {
    String name

    static mapping = {
        id column: 'protocolid'
        name column:'protocolname'

        table 'labprotocol'
        version false
    }
}
