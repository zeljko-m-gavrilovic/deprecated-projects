<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN"
"http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">

<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<%@ include file="/common/taglibs.jsp"%>
<%@ include file="/common/meta.jsp"%>
<%@ include file="/common/scripts.jsp"%>
<%@ include file="/common/styles.jsp"%>
<title>Employment Application</title>

<!-- Meta Tags -->
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<meta name="generator" content="Wufoo.com" />

<!-- CSS -->
<link rel="stylesheet" href="themes/blue/css/structure.css"
	type="text/css" />
<link rel="stylesheet" href="themes/blue/css/form.css" type="text/css" />
<link rel="stylesheet" href="themes/blue/css/theme.css" type="text/css" />

<!-- JavaScript -->
<script type="text/javascript" src="themes/blue/scripts/wufoo.js"></script>
</head>

<body id="public">
<div id="container">

<h1 id="logo"><a href="http://wufoo.com" title="Powered by Wufoo">Wufoo</a>
</h1>

<form id="form73" name="form73" class="wufoo  page" autocomplete="off"
	enctype="multipart/form-data" method="post" action="#public">

<div class="info">
<h2>Employment Application</h2>
<div>Infinity Box Inc.</div>
</div>

<ul>



	<li id="foli17" class="      "><label class="desc" id="title17"
		for="Field17"> Which position are you applying for? <span
		id="req_17" class="req">*</span> </label>
	<div><select id="Field17" name="Field17"
		class="field select medium" tabindex="1">
		<option value="" selected="selected"></option>
		<option value="Interface Designer">Interface Designer</option>
		<option value="Software Engineer">Software Engineer</option>
		<option value="System Administrator">System Administrator</option>
		<option value="Office Manager">Office Manager</option>
	</select></div>
	</li>


	<li id="foli19" class="     "><label class="desc" id="title19"
		for="Field19_0"> Are you willing to relocate? <span
		id="req_19" class="req">*</span> </label>
	<div><input id="radioDefault_19" name="Field19" type="hidden"
		value="" /> <span> <input id="Field19_0" name="Field19"
		type="radio" class="field radio" value="Yes" tabindex="2"
		checked="checked" /> <label class="choice" for="Field19_0">
	Yes</label> </span> <span> <input id="Field19_1" name="Field19" type="radio"
		class="field radio" value="No" tabindex="3" /> <label class="choice"
		for="Field19_1"> No</label> </span></div>
	</li>


	<li id="foli20" class="date">
		<label class="desc"
			id="title20" for="Field20"> When can you start? <span
			id="req_20" class="req">*</span> 
		</label> 
		<span> 
			<input id="dateField"
				name="dateField" type="text" class="field text" value="" size="10"
				maxlength="10" tabindex="10" />
		</span>
		<span>
			<input id="timeField"
				name="timeField" type="text" class="field text" value="" size="5"
				maxlength="5" tabindex="5"/> 
		</span>
		 <label for="Field20">dd.mm.yyyy</label> 
	 	<span id="cal20"> 
			<img id="pick20" class="datepicker"
			src="themes/blue/images/calendar.png" alt="Pick a date." /> 
		</span> 
		<script type="text/javascript">
			Calendar.setup({
			inputField	 : "dateField",
			button		 : "pick20",
			ifFormat	 : "%d.%m.%Y"	
		});
		</script>
	</li>


	<li id="foli14" class="      "><label class="desc" id="title14"
		for="Field14"> Portfolio Web Site </label>
	<div><input id="Field14" name="Field14" type="text"
		class="field text large" value="http://" maxlength="255" tabindex="7" />
	</div>
	</li>


	<li id="foli12" class="altInstruct      "><label class="desc"
		id="title12" for="Field12"> Attach a Copy of Your Resume </label>
	<div><input id="Field12" name="Field12" type="file"
		class="field file large" tabindex="8" /></div>
	<p class="instruct" id="instruct12"><small>Word or PDF
	Documents Only</small></p>
	</li>



	<li id="foli16" class="     "><label class="desc" id="title16"
		for="Field16"> Salary Requirements </label> <span class="symbol">$</span>
	<span> <input id="Field16" name="Field16" type="text"
		class="field text currency" value="" size="10" tabindex="9" /> <label
		for="Field16">Dollars</label> </span> <span class="symbol">.</span> <span>
	<input id="Field16-1" name="Field16-1" type="text" class="field text"
		value="" size="2" maxlength="2" tabindex="10" /> <label
		for="Field16-1">Cents</label> </span></li>


	<li id="foli22" class="     ">
		<label class="desc" id="title22"
		for="Field22"> Name <span id="req_22" class="req">*</span> </label> <span>
	<input id="Field22" name="Field22" type="text" class="field text fn"
		value="" size="8" tabindex="11" /> <label for="Field22">First</label>
	</span> <span> <input id="Field23" name="Field23" type="text"
		class="field text ln" value="" size="14" tabindex="12" /> <label
		for="Field23">Last</label> </span></li>


	<li id="foli13" class="     "><label class="desc" id="title13"
		for="Field13"> Email Address <span id="req_13" class="req">*</span>
	</label>
	<div><input id="Field13" name="Field13" type="text"
		class="field text large" value="" maxlength="255" tabindex="13" /></div>
	</li>


	<li id="foli25" class="phone      "><label class="desc"
		id="title25" for="Field25"> Phone <span id="req_25"
		class="req">*</span> </label> <span> <input id="Field25" name="Field25"
		type="text" class="field text" value="" size="3" maxlength="3"
		tabindex="14" /> <label for="Field25">###</label> </span> <span
		class="symbol">-</span> <span> <input id="Field25-1"
		name="Field25-1" type="text" class="field text" value="" size="3"
		maxlength="3" tabindex="15" /> <label for="Field25-1">###</label> </span> <span
		class="symbol">-</span> <span> <input id="Field25-2"
		name="Field25-2" type="text" class="field text" value="" size="4"
		maxlength="4" tabindex="16" /> <label for="Field25-2">####</label> </span></li>




	<li class="buttons ">
	<div><input type="hidden" name="currentPage" id="currentPage"
		value="dB5YAYUJLThQ1vViLqkRtO8PC6nWmLuPsz2BRQNT4gw=" /> <input
		id="saveForm" name="saveForm" class="btTxt submit" type="submit"
		value="Submit" /></div>
	</li>

	<li style="display: none"><label for="comment">Do Not Fill
	This Out</label> <textarea name="comment" id="comment" rows="1" cols="1"></textarea>
	<input type="hidden" id="idstamp" name="idstamp" value="" /></li>
</ul>
</form>

</div>
<!--container-->
<img id="bottom" src="images/bottom.png" alt="" />

<a href="http://wufoo.com" title="Designed with Wufoo"> <img
	src="images/powerlogo.png" alt="Designed with Wufoo"/>
</a>
</body>
</html>