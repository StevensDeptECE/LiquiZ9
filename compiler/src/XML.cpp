#if 0
xml <<
R"(<?xml version="1.0" encoding="UTF-8"?>
<questestinterop xmlns="http://www.imsglobal.org/xsd/ims_qtiasiv1p2" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" xsi:schemaLocation="http://www.imsglobal.org/xsd/ims_qtiasiv1p2 http://www.imsglobal.org/xsd/ims_qtiasiv1p2p1.xsd">
  <assessment ident="liquiz)" << uuid << "\" title=\"" << quizName << 
R("">
<qtimetadata>
  <qtimetadatafield>
    <fieldlabel>cc_maxattempts</fieldlabel>
    <fieldentry>1</fieldentry>
  </qtimetadatafield>
</qtimetadata>
    <section ident="root_section">
      <section ident="liquiz") << uuid << R(" title="Fill in the missing ARM">
        <selection_ordering>
          <selection>
            <selection_number>1</selection_number>
            <selection_extension>
              <points_per_item>1.0</points_per_item>
            </selection_extension>
          </selection>
        </selection_ordering>
");
#endif
void LiQuizCompiler::saveXML() {
  #if 0
  xml <<
R("<item ident="liquiz") << uuid << 
R(" title="Question">
  <itemmetadata>
    <qtimetadata>
      <qtimetadatafield>
        <fieldlabel>question_type</fieldlabel>
        <fieldentry>fill_in_multiple_blanks_question</fieldentry>
      </qtimetadatafield>
      <qtimetadatafield>
        <fieldlabel>points_possible</fieldlabel>
        <fieldentry>1.0</fieldentry>
      </qtimetadatafield>
      <qtimetadatafield>
        <fieldlabel>original_answer_ids</fieldlabel>
        <fieldentry>4671,4417,1033,1118,25,7804,9862</fieldentry>
      </qtimetadatafield>
      <qtimetadatafield>
        <fieldlabel>assessment_question_identifierref</fieldlabel>
        <fieldentry>gf47b767d37e06559ff801f2d253307ba</fieldentry>
      </qtimetadatafield>
    </qtimetadata>
  </itemmetadata>
<presentation>
  <material>
    <mattext texttype="text/html"> ");
#endif
#if 0
xml << "<p>" << "Fill in the missing assembler instructions and parameters. Please enter everything in lowercase" << "</p>";
xml << R("<table style="width: 80%">
<tr><td style="width: 40%"><pre style="font-size: 14pt;">_Z3sumPii:</pre></td></tr>
<tr><td></td><td style="width: 20%"><pre style="font-size: 14pt">mov</pre></td><td style="width: 20%">
<pre style="font-size: 14pt">r2,</pre></td><td style="width: 20%"><pre style="font-size: 14pt">#0</pre></td></tr> ");
#endif

#if 0
</mattext>
</material>
  <response_lid ident="response_i0">
    <material>
      <mattext>i0</mattext>
    </material>
             <render_choice>
                <response_label ident="4671">
                  <material>
                    <mattext texttype="text/plain">ldr</mattext>
                  </material>
                </response_label>
              </render_choice>
            </response_lid>
            <response_lid ident="response_p0">
              <material>
                <mattext>p0</mattext>
              </material>
              <render_choice>
                <response_label ident="4417">
                  <material>
                    <mattext texttype="text/plain">[r0]</mattext>
                  </material>
                </response_label>
              </render_choice>
            </response_lid>
            <response_lid ident="response_i2">
              <material>
                <mattext>i2</mattext>
              </material>
              <render_choice>
                <response_label ident="1033">
                  <material>
                    <mattext texttype="text/plain">add</mattext>
                  </material>
                </response_label>
                <response_label ident="1118">
                  <material>
                    <mattext texttype="text/plain">subs</mattext>
                  </material>
                </response_label>
              </render_choice>
            </response_lid>
            <response_lid ident="response_i3">
              <material>
                <mattext>i3</mattext>
              </material>
              <render_choice>
                <response_label ident="25">
                  <material>
                    <mattext texttype="text/plain">r1</mattext>
                  </material>
                </response_label>
                <response_label ident="7804">
                  <material>
                    <mattext texttype="text/plain">bne</mattext>
                  </material>
                </response_label>
                <response_label ident="9862">
                  <material>
                    <mattext texttype="text/plain">bgt</mattext>
                  </material>
                </response_label>
              </render_choice>
            </response_lid>
          </presentation>
          <resprocessing>
            <outcomes>
              <decvar maxvalue="100" minvalue="0" varname="SCORE" vartype="Decimal"/>
            </outcomes>
            <respcondition>
              <conditionvar>
                <varequal respident="response_i0">4671</varequal>
              </conditionvar>
              <setvar varname="SCORE" action="Add">25.00</setvar>
            </respcondition>
            <respcondition>
              <conditionvar>
                <varequal respident="response_p0">4417</varequal>
              </conditionvar>
              <setvar varname="SCORE" action="Add">25.00</setvar>
            </respcondition>
            <respcondition>
              <conditionvar>
                <varequal respident="response_i2">1033</varequal>
              </conditionvar>
              <setvar varname="SCORE" action="Add">25.00</setvar>
            </respcondition>
            <respcondition>
              <conditionvar>
                <varequal respident="response_i3">25</varequal>
              </conditionvar>
              <setvar varname="SCORE" action="Add">25.00</setvar>
            </respcondition>
          </resprocessing>
        </item>
#endif
}

int main() {
  ofstream f("testquiz.xml");
}