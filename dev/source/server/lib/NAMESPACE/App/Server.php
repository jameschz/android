<?php
/**
 * NAMESPACE App
 *
 * @category   NAMESPACE
 * @package    NAMESPACE_App
 * @author     James.Huang <huangjuanshi@snda.com>
 * @license    http://www.apache.org/licenses/LICENSE-2.0
 * @version    $Id$
 */

require_once 'Hush/Service.php';

/**
 * @package NAMESPACE_App
 */
class NAMESPACE_App_Server extends Hush_Service
{
	/**
	 * @var array
	 */
	protected $_msgs = array();
	
	/**
	 * Initialize mongo dao
	 * 
	 * @return array
	 */
	public function __init ()
	{
		parent::__init();
		
		// init dao
		require_once 'NAMESPACE/Dao.php';
		$this->dao = new NAMESPACE_Dao();
		
		// init url
		require_once 'NAMESPACE/Util/Url.php';
		$this->url = new NAMESPACE_Util_Url();
		
		// init session
		require_once 'NAMESPACE/Util/Session.php';
		$this->session = new NAMESPACE_Util_Session();
	}
	
	/**
	 * Logging mongo dao
	 * 
	 * @return array
	 */
	public function __done ()
	{
		parent::__done();
	}
	
	/**
	 * Forward page by header redirection
	 * J2EE like method's name :)
	 * 
	 * @param string $url
	 * @return void
	 */
	public function forward ($url)
	{
		// append sid for url
		Hush_Util::headerRedirect($this->url->format($url));
		exit;
	}
	
	/**
	 * 
	 */
	public function render ($code, $message, $result = '')
	{
		// filter by datamap
		if (is_array($result)) {
			foreach ((array) $result as $name => $data) {
				// Object list
				if (strpos($name, '.list')) {
					$model = trim(str_replace('.list', '', $name));
					foreach ((array) $data as $k => $v) {
						$result[$name][$k] = M($model, $v);
					}
				// Object
				} else {
					$model = trim($name);
					$result[$name] = M($model, $data);
				}
			}
		}
		// print json code
		echo json_encode(array(
			'code'		=> $code,
			'message'	=> $message,
			'result'	=> $result
		));
		exit;
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////
	// protected method
	
	/**
	 * @ingore
	 */
	public function doAuth ()
	{
		if (!isset($_SESSION['customer'])) {
			$this->render('10001', 'Please login firstly.');
		} else {
			$this->customer = $_SESSION['customer'];
		}
	}
	
	/**
	 * @ingore
	 */
	public function doAuthAdmin ()
	{
		if (!isset($_SESSION['admin'])) {
			$this->forward($this->apiAuth); // auth action
		} else {
			$this->admin = $_SESSION['admin'];
		}
	}
}
