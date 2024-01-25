import commons.serviceportal.forms.FormValidator
import spock.lang.Specification

class ValidFormSpecification extends Specification {
  def "Verify form is valid"() {
    when:
    List<String> forms = [
            "govTecEventSubmitForm_ApplicantForm-v1.0-de.json", // TODO: Update the file names
            "CustomerName_ProcessName_PreliminaryForm-v1.0-de.json",
            "govTecEventSubmitForm_SummaryForm-v1.0-de.json",
    ]
    forms.each { form ->
      new FormValidator(getClass().getResourceAsStream(form).text).validate()
    }

    then:
    noExceptionThrown()
  }
}
