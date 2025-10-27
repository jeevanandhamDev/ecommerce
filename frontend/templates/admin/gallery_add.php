<?php
include 'connection.php';
include 'admin-auth.php';

if (isset($_POST['upload'])) {
    $targetDir = "uploads/gallery/";
    if (!is_dir($targetDir)) {
        mkdir($targetDir, 0777, true);
    }

    $timestamp = time();
    $imageName = $timestamp . "_" . basename($_FILES["image"]["name"]);
    $targetFile = $targetDir . $imageName;

    if (move_uploaded_file($_FILES["image"]["tmp_name"], $targetFile)) {
        $safePath = mysqli_real_escape_string($conn, $targetFile);
        $conn->query("INSERT INTO gallery (images) VALUES ('$safePath')");
        echo "<script>alert('Image uploaded successfully!');</script>";
    } else {
        echo "<script>alert('Failed to upload image.');</script>";
    }
}
?>


<!doctype html><html class="fixed">
	

<head>
		<!-- Basic -->
		<meta charset="UTF-8">
		<title>Admin - Manna Chemicals and Drugs Pvt Ltd.,</title>
		<meta name="keywords" content="Manna Chemicals and Drugs Pvt Ltd" />
		<meta name="description" content="Manna Chemicals and Drugs Pvt Ltd">
		<meta name="author" content="Manna Chemicals and Drugs Pvt Ltd">
		<!-- Mobile Metas -->
		<meta name="viewport" content="width=device-width, initial-scale=1.0, maximum-scale=1.0, user-scalable=no" />
		<!-- Web Fonts  -->		<link href="https://fonts.googleapis.com/css?family=Poppins:300,400,500,600,700,800|Shadows+Into+Light" rel="stylesheet" type="text/css">
		<!-- Vendor CSS -->
		<link rel="stylesheet" href="vendor/bootstrap/css/bootstrap.css" />		<link rel="stylesheet" href="vendor/animate/animate.compat.css">		<link rel="stylesheet" href="vendor/font-awesome/css/all.min.css" />		<link rel="stylesheet" href="vendor/boxicons/css/boxicons.min.css" />		<link rel="stylesheet" href="vendor/magnific-popup/magnific-popup.css" />		<link rel="stylesheet" href="vendor/bootstrap-datepicker/css/bootstrap-datepicker3.css" />
		<!-- Specific Page Vendor CSS -->
		<link rel="stylesheet" href="vendor/jquery-ui/jquery-ui.css" />		<link rel="stylesheet" href="vendor/jquery-ui/jquery-ui.theme.css" />		<link rel="stylesheet" href="vendor/bootstrap-multiselect/css/bootstrap-multiselect.css" />		<link rel="stylesheet" href="vendor/morris/morris.css" />
		<!-- Theme CSS -->
		<link rel="stylesheet" href="css/theme.css" />
		<!-- Theme Custom CSS -->
		<link rel="stylesheet" href="css/custom.css">
		<!-- Head Libs -->
		<script src="vendor/modernizr/modernizr.js"></script>
		<script src="master/style-switcher/style.switcher.localstorage.js"></script>
	</head>
	<body>
		<section class="body">
			<!-- start: header -->
			
			<header class="header">
				<div class="logo-container">
					<a href="dashboard.php" class="logo">				
						<img src="img/logo.png" width="75" height="35" alt="manna chemicals" />
					</a>
					<div class="d-md-none toggle-sidebar-left" data-toggle-class="sidebar-left-opened" data-target="html" data-fire-event="sidebar-left-opened">
						<i class="fas fa-bars" aria-label="Toggle sidebar"></i>
					</div>
				</div>
				
			</header>

			<!-- end: header -->
			<div class="inner-wrapper">
				<!-- start: sidebar -->
				<?php include 'menu.php';?>
				<!-- end: sidebar -->
				<section role="main" class="content-body">
					<header class="page-header">
						<h2>Gallery</h2>
						<div class="right-wrapper text-end">
							<ol class="breadcrumbs">
								<li>
									<a href="dashboard.php">
										<i class="bx bx-home-alt"></i>
									</a>
								</li>
								<!-- <li><span>Layouts</span></li>
								<li><span>Default</span></li> -->
							</ol>
							<a class="sidebar-right-toggle" data-open="sidebar-right" title="calender"><i class="fas fa-chevron-left"></i></a>
						</div>
					</header>
					<!-- start: page -->
					<div class="row"  >
						
						<div class="col-lg-11">
                        <form action="gallery_add.php" method="POST" enctype="multipart/form-data">
						<div class="mb-3">
							<label class="form-label">Select Image</label>
							<input type="file" name="image" class="form-control" required>
						</div>
						<button type="submit" name="upload" class="btn btn-primary">Upload</button>
					</form>

					
						</div>
						
						</div>
					<!-- end: page -->
				</section>
			</div>
			<aside id="sidebar-right" class="sidebar-right">
				<div class="nano">
					<div class="nano-content">
						<a href="#" class="mobile-close d-md-none">
							Collapse <i class="fas fa-chevron-right"></i>
						</a>
						<div class="sidebar-right-wrapper">
							<div class="sidebar-widget widget-calendar">
								<h6>Upcoming Tasks</h6>
								<div data-plugin-datepicker data-plugin-skin="dark"></div>
								<ul>
									<li>
										<time datetime="2023-04-19T00:00+00:00">04/19/2023</time>
										<span>Company Meeting</span>
									</li>
								</ul>
							</div>
							<div class="sidebar-widget widget-friends">
								<h6>Friends</h6>
								<ul>
									<li class="status-online">
										<figure class="profile-picture">
											<img src="img/%21sample-user.jpg" alt="Joseph Doe" class="rounded-circle">
										</figure>
										<div class="profile-info">
											<span class="name">Joseph Doe Junior</span>
											<span class="title">Hey, how are you?</span>
										</div>
									</li>
									<li class="status-online">
										<figure class="profile-picture">
											<img src="img/%21sample-user.jpg" alt="Joseph Doe" class="rounded-circle">
										</figure>
										<div class="profile-info">
											<span class="name">Joseph Doe Junior</span>
											<span class="title">Hey, how are you?</span>
										</div>
									</li>
									<li class="status-offline">
										<figure class="profile-picture">
											<img src="img/%21sample-user.jpg" alt="Joseph Doe" class="rounded-circle">
										</figure>
										<div class="profile-info">
											<span class="name">Joseph Doe Junior</span>
											<span class="title">Hey, how are you?</span>
										</div>
									</li>
									<li class="status-offline">
										<figure class="profile-picture">
											<img src="img/%21sample-user.jpg" alt="Joseph Doe" class="rounded-circle">
										</figure>
										<div class="profile-info">
											<span class="name">Joseph Doe Junior</span>
											<span class="title">Hey, how are you?</span>
										</div>
									</li>
								</ul>
							</div>
						</div>
					</div>
				</div>
			</aside>
		</section>
		<!-- Vendor -->
		<script src="vendor/jquery/jquery.js"></script>		<script src="vendor/jquery-browser-mobile/jquery.browser.mobile.js"></script>		<script src="vendor/jquery-cookie/jquery.cookie.js"></script>		<script src="master/style-switcher/style.switcher.js"></script>		<script src="vendor/popper/umd/popper.min.html"></script>		<script src="vendor/bootstrap/js/bootstrap.bundle.min.js"></script>		<script src="vendor/bootstrap-datepicker/js/bootstrap-datepicker.js"></script>		<script src="vendor/common/common.js"></script>		<script src="vendor/nanoscroller/nanoscroller.js"></script>		<script src="vendor/magnific-popup/jquery.magnific-popup.js"></script>		<script src="vendor/jquery-placeholder/jquery.placeholder.js"></script>
		<!-- Specific Page Vendor -->
		<script src="vendor/jquery-ui/jquery-ui.js"></script>		<script src="vendor/jqueryui-touch-punch/jquery.ui.touch-punch.js"></script>		<script src="vendor/jquery-appear/jquery.appear.js"></script>		<script src="vendor/bootstrap-multiselect/js/bootstrap-multiselect.js"></script>		<script src="vendor/jquery.easy-pie-chart/jquery.easypiechart.js"></script>		<script src="vendor/flot/jquery.flot.js"></script>		<script src="vendor/flot.tooltip/jquery.flot.tooltip.js"></script>		<script src="vendor/flot/jquery.flot.pie.js"></script>		<script src="vendor/flot/jquery.flot.categories.js"></script>		<script src="vendor/flot/jquery.flot.resize.js"></script>		<script src="vendor/jquery-sparkline/jquery.sparkline.js"></script>		<script src="vendor/raphael/raphael.js"></script>		<script src="vendor/morris/morris.js"></script>		<script src="vendor/gauge/gauge.js"></script>		<script src="vendor/snap.svg/snap.svg.js"></script>		<script src="vendor/liquid-meter/liquid.meter.js"></script>		<script src="vendor/jqvmap/jquery.vmap.js"></script>		<script src="vendor/jqvmap/data/jquery.vmap.sampledata.js"></script>		<script src="vendor/jqvmap/maps/jquery.vmap.world.js"></script>		<script src="vendor/jqvmap/maps/continents/jquery.vmap.africa.js"></script>		<script src="vendor/jqvmap/maps/continents/jquery.vmap.asia.js"></script>		<script src="vendor/jqvmap/maps/continents/jquery.vmap.australia.js"></script>		<script src="vendor/jqvmap/maps/continents/jquery.vmap.europe.js"></script>		<script src="vendor/jqvmap/maps/continents/jquery.vmap.north-america.js"></script>		<script src="vendor/jqvmap/maps/continents/jquery.vmap.south-america.js"></script>
		<!-- Theme Base, Components and Settings -->
		<script src="js/theme.js"></script>
		<!-- Theme Custom -->
		<script src="js/custom.js"></script>
		<!-- Theme Initialization Files -->
		<script src="js/theme.init.js"></script>
		<!-- Analytics to Track Preview Website -->
		<script>
		  (function(i,s,o,g,r,a,m){i['GoogleAnalyticsObject']=r;i[r]=i[r]||function(){
		  (i[r].q=i[r].q||[]).push(arguments)},i[r].l=1*new Date();a=s.createElement(o),
		  m=s.getElementsByTagName(o)[0];a.async=1;a.src=g;m.parentNode.insertBefore(a,m)
		  })(window,document,'script','../../../../www.google-analytics.com/analytics.js','ga');
		  ga('create', 'UA-42715764-8', 'auto');
		  ga('send', 'pageview');
		</script>
		<!-- Examples -->
		<script src="js/examples/examples.dashboard.js"></script>
	</body>

<!-- Mirrored from www.okler.net/previews/porto-admin/4.3.0/layouts-default.html by HTTrack Website Copier/3.x [XR&CO'2014], Thu, 13 Mar 2025 09:39:57 GMT -->
</html>