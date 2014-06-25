<?php
/**
 * Weibo App
 *
 * @category   Weibo
 * @package    Weibo_App_Server
 * @author     James.Huang <huangjuanshi@snda.com>
 * @license    http://www.apache.org/licenses/LICENSE-2.0
 * @version    $Id$
 */

require_once 'Weibo/App/Server.php';
require_once 'Weibo/Util/Image.php';

/**
 * @package Weibo_App_Server
 */
class ImageServer extends Weibo_App_Server
{
	/**
	 * ---------------------------------------------------------------------------------------------
	 * > 全局设置：
	 * <code>
	 * </code>
	 * ---------------------------------------------------------------------------------------------
	 */
	public function __init ()
	{
		parent::__init();
		
		// get image host
		$this->imageHost = __HOST_WEBSITE;
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////
	// service api methods
	
	/**
	 * ---------------------------------------------------------------------------------------------
	 * > 接口说明：查看用户头像接口
	 * <code>
	 * URL地址：/image/faceView
	 * 提交方式：GET
	 * 参数#1：faceId，类型：STRING，必须：YES
	 * </code>
	 * ---------------------------------------------------------------------------------------------
	 * @title 查看用户头像接口
	 * @action /image/faceView
	 * @params faceId 0 STRING
	 * @method get
	 */
	public function faceViewAction ()
	{
		$faceId = explode(',', $this->param('faceId'));
		$faceCount = count($faceId);
		if ($faceCount == 1) {
			$id = intval($faceId[0]);
			$faceItem = Weibo_Util_Image::getFaceImage($id);
			$this->render('10000', 'Get face ok', array(
				'Image' => $faceItem
			));
		} elseif ($faceCount > 1) {
			$faceList = array();
			foreach ($faceId as $id) {
				$faceList[] = Weibo_Util_Image::getFaceImage($id);
			}
			$this->render('10000', 'Get face list ok', array(
				'Image.list' => $faceList
			));
		} else {
			$this->render('10009', 'Get face failed');
		}
	}
	
	/**
	 * ---------------------------------------------------------------------------------------------
	 * > 接口说明：头像列表接口
	 * <code>
	 * URL地址：/image/faceList
	 * 提交方式：GET
	 * </code>
	 * ---------------------------------------------------------------------------------------------
	 * @title 头像列表接口
	 * @action /image/faceList
	 * @method get
	 */
	public function faceListAction ()
	{
		// valid face ids
		$faceId = array(0,1,2,3,4,5,6,7,8,9,10,11,12,13,14);
		// get face images
		$faceList = array();
		foreach ($faceId as $id) {
			$faceList[] = Weibo_Util_Image::getFaceImage($id);
		}
		$this->render('10000', 'Get face list ok', array(
			'Image.list' => $faceList
		));
	}
}