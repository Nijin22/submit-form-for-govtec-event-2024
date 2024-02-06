package govTecEventSubmitForm

import de.seitenbau.serviceportal.scripting.api.v1.ScriptingApiV1
import de.seitenbau.serviceportal.scripting.api.v1.form.content.BinaryContentV1
import de.seitenbau.serviceportal.scripting.api.v1.form.content.FormContentV1

ScriptingApiV1 api = apiV1 // Variable is automatically set by Serviceportal process engine
FormContentV1 applicantForm = api.getVariable("applicantForm") as FormContentV1

// recipient
// this is the postfach handle of the 'testUserPST' user on the integration system of bund.id
String workshopGroup = applicantForm.fields."mainGroup:0:workshopGroup".value
String recipient
assert workshopGroup != null && !workshopGroup.isAllWhitespace()
switch (workshopGroup) {
  case "taxi":
    recipient = '{"@type":"nkb","id":"9ad8311b-d3d6-4570-9326-b8bd5f7f44c1"}' // "Simones public service team" on int.bund.id
    break
  case "strasse":
    recipient = '{"@type":"nkb","id":"c8fab851-1207-4a8a-8a7f-6af61173ff01"}' // "testUserPST" on int.bund.id
    break
  default:
    throw new IllegalArgumentException("Unknown workshop Group '$workshopGroup'.")
}
api.setVariable("messageRecipient", recipient)

// subject
String teamName = applicantForm.fields."mainGroup:0:teamName".value
String subject = "GovTecEvent 2024: ${workshopGroup}-Gruppe: $teamName"
api.setVariable("messageSubject", subject)

// messageAttachments
BinaryContentV1 file = (applicantForm.fields."mainGroup:0:formFile".value as List<BinaryContentV1>).first()
String teamNameSanitized = teamName.replaceAll(/[^a-zA-Z0-9]/, "") // replace non-letters and non-digits
file.uploadedFilename = "form-${workshopGroup}-${teamNameSanitized}-rename_me_to_.json.txt"
api.setVariable("messageAttachments", file)
