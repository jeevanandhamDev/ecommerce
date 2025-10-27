<?php
include 'connection.php';
include 'admin-auth.php';

// Handle image deletion
if (isset($_GET['delete'])) {
    $id = intval($_GET['delete']);
    $result = $conn->query("SELECT images FROM gallery WHERE id=$id");
    $row = $result->fetch_assoc();
    if ($row) {
        @unlink($row['images']);
        $conn->query("DELETE FROM gallery WHERE id=$id");
        $msg = "<div class='alert alert-success'>Image deleted successfully!</div>";
    } else {
        $msg = "<div class='alert alert-danger'>Image not found.</div>";
    }
}

// Pagination setup
$limit = 10; // Number of images per page
$page = isset($_GET['page']) ? max(1, intval($_GET['page'])) : 1;
$offset = ($page - 1) * $limit;

// Get total number of images
$total_result = $conn->query("SELECT COUNT(*) as total FROM gallery");
$total_row = $total_result->fetch_assoc();
$total_images = $total_row['total'];
$total_pages = ceil($total_images / $limit);

// Fetch paginated images
$images = $conn->query("SELECT * FROM gallery ORDER BY id DESC LIMIT $limit OFFSET $offset");
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
    <div class="table-responsive">
        <table class="table table-bordered table-striped">
            <thead class="table-dark">
                <tr>
                    <th>No.</th>
                    <th>Image</th>
                    <th>File Path</th>
                    <th>Action</th>
                </tr>
            </thead>
            <tbody>
                <?php
                $i = 1;
                $images = $conn->query("SELECT * FROM gallery ORDER BY id DESC");
                while ($row = $images->fetch_assoc()) :
                ?>
                    <tr>
                        <td><?= $i++; ?></td>
                        <td>
                            <img src="<?= $row['images']; ?>" style="height: 80px; width: auto;" alt="Gallery Image">
                        </td>
                        <td><?= $row['images']; ?></td>
                        <td>
                            <a href="?delete=<?= $row['id']; ?>" class="btn btn-sm btn-danger" onclick="return confirm('Are you sure you want to delete this image?');">Delete</a>
                        </td>
                    </tr>
                <?php endwhile; ?>
            </tbody>
        </table>
		<div class="mt-3 text-center">
			<nav>
				<ul class="pagination justify-content-center">
					<?php if ($page > 1): ?>
						<li class="page-item"><a class="page-link" href="?page=<?= $page - 1; ?>">Previous</a></li>
					<?php endif; ?>

					<?php for ($i = 1; $i <= $total_pages; $i++): ?>
						<li class="page-item <?= ($i == $page) ? 'active' : ''; ?>">
							<a class="page-link" href="?page=<?= $i; ?>"><?= $i; ?></a>
						</li>
					<?php endfor; ?>

					<?php if ($page < $total_pages): ?>
						<li class="page-item"><a class="page-link" href="?page=<?= $page + 1; ?>">Next</a></li>
					<?php endif; ?>
				</ul>
			</nav>
		</div>

    </div>
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


</html>