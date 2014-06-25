<?php
/**
 * Weibo Util
 *
 * @category   Weibo
 * @package    Weibo_Util
 * @author     James.Huang <huangjuanshi@snda.com>
 * @license    http://www.apache.org/licenses/LICENSE-2.0
 * @version    $Id$
 */

/**
 * @package Weibo_Util
 */
class Weibo_Util_Image
{
	public static function getFaceUrl ($id) 
	{
		$facePath = __HOST_WEBSITE . '/faces/default';
		return $facePath . '/face_' . $id . '.png';
	}
	
	public static function getFaceImage ($id) 
	{
		return array(
			'id' => $id,
			'url' => self::getFaceUrl($id),
			'type' => 'png',
		);
	}
}