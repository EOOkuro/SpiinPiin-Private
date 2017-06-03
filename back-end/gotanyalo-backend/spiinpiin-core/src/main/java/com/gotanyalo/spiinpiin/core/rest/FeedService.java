/**
 * 
 */
package com.gotanyalo.spiinpiin.core.rest;

import java.io.Serializable;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.gotanyalo.spiinpiin.core.data.RArticle;
import com.gotanyalo.spiinpiin.core.data.RArticleDetail;
import com.gotanyalo.spiinpiin.core.data.RComment;
import com.gotanyalo.spiinpiin.core.data.TArticle;
import com.gotanyalo.spiinpiin.core.data.TComment;
import com.gotanyalo.spiinpiin.core.data.TResult;
import com.gotanyalo.spiinpiin.core.data.TSession;
import com.gotanyalo.spiinpiin.core.exceptions.SpiinPiinBaseException;
import com.gotanyalo.spiinpiin.core.service.ISpiinPiinManagementLocal;

/**
 * @author otkoth
 *
 */
@Path("/feed")
@Stateless
public class FeedService implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
		
	@Inject
    private Logger logger;
	
	@Inject
	private ISpiinPiinManagementLocal insMgt;
	
	/*
	 * 3.CHANNELS FEED SUMMARY
SEND:  { sessionkey:string,latitude:double,longitude:double}
Return: { 
	spinnername,
	spinner_photo_thumbnail,
	article_summary,
	article_location,
	article_thumbnail/image,
	comment_count,
	likes_count,
	dislikes_count,
	is_my_favorite,
	date_posted,
	day_posted,
	time_posted
	}
4.CHANNELS FEED DETAILS
SEND:  { sessionkey:string,latitude:double,longitude:double}
Return: { 	
	full_article,
	article_image,
	view_count,
	comments{
		[{username,
		comment,
		i_liked_comment:boolean,
		comment_date_posted
		comment_time_posted}
		,....
		]
	}
	}
3.POST COMMENT
SEND:  { sessionkey,article_id,comment}
	 */

	@GET
	@Path("/lacomment/{aid}/{page}/{size}")
	@Produces(MediaType.APPLICATION_JSON)
	public TResult<List<RComment>> listComment(
			@PathParam("aid") final int aid,
			@PathParam("page") final int page,
			@PathParam("size") final int size,
			@HeaderParam("Latitude") Double latitude,
			@HeaderParam("Longitude") Double longitude,
			@HeaderParam("AuthorizationKey") String sessionKey) {
		return null; //new TResult<List<Article>>(1, insMgt.listCountry(), null);
	}
	
	@GET
	@Path("/garticaldetail/{aid}")
	@Produces(MediaType.APPLICATION_JSON)
	public TResult<RArticleDetail> getRArticleDetail(
			@PathParam("aid") final int aid,
			@HeaderParam("Latitude") Double latitude,
			@HeaderParam("Longitude") Double longitude,
			@HeaderParam("AuthorizationKey") String sessionKey) {
		return null; //new TResult<List<Article>>(1, insMgt.listCountry(), null);
	}
	
	@GET
	@Path("/larticle/{page}/{size}")
	@Produces(MediaType.APPLICATION_JSON)
	public TResult<List<RArticle>> listArticle(
			@PathParam("page") final int page,
			@PathParam("size") final int size,
			@HeaderParam("Latitude") Double latitude,
			@HeaderParam("Longitude") Double longitude,
			@HeaderParam("AuthorizationKey") String sessionKey) {
		return null; //new TResult<List<Article>>(1, insMgt.listCountry(), null);
	}
	
	@POST
    @Path("/like/{aid}/{islike}")
    @Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public TResult<Boolean> like(
			@PathParam("aid") final int aid,
			@PathParam("islike") final boolean islike,
			@HeaderParam("AuthorizationKey") String sessionKey,
			@Context HttpServletRequest request){		
		return this.excecute(sessionKey, new IJxRsExecute() {
			
			@SuppressWarnings("unchecked")
			@Override
			public TResult<Boolean> run(TSession session) 
					throws SpiinPiinBaseException {
				// TODO Auto-generated method stub
				return null;
			}
		});
	}
	
	@POST
    @Path("/likec/{cid}/{islike}")
    @Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public TResult<Boolean> likeComment(
			@PathParam("cid") final int cid,
			@PathParam("islike") final boolean islike,
			@HeaderParam("AuthorizationKey") String sessionKey,
			@Context HttpServletRequest request){		
		return this.excecute(sessionKey, new IJxRsExecute() {
			
			@SuppressWarnings("unchecked")
			@Override
			public TResult<Boolean> run(TSession session) throws SpiinPiinBaseException {
				// TODO Auto-generated method stub
				return null;
			}
		});
	}
	
	///////
	@POST
    @Path("/favorite/{aid}/{islike}")
    @Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public TResult<Boolean> isFavourite(
			@PathParam("aid") final int aid,
			@PathParam("islike") final boolean islike,
			@HeaderParam("AuthorizationKey") String sessionKey,
			@Context HttpServletRequest request){		
		return this.excecute(sessionKey, new IJxRsExecute() {
			
			@SuppressWarnings("unchecked")
			@Override
			public TResult<Boolean> run(TSession session) 
					throws SpiinPiinBaseException {
				// TODO Auto-generated method stub
				return null;
			}
		});
	}
	
	@POST
    @Path("/favorite/{cid}/{islike}")
    @Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public TResult<Boolean> isFavouriteComment(
			@PathParam("cid") final int cid,
			@PathParam("islike") final boolean islike,
			@HeaderParam("AuthorizationKey") String sessionKey,
			@Context HttpServletRequest request){		
		return this.excecute(sessionKey, new IJxRsExecute() {
			
			@SuppressWarnings("unchecked")
			@Override
			public TResult<Boolean> run(TSession session) throws SpiinPiinBaseException {
				// TODO Auto-generated method stub
				return null;
			}
		});
	}
	///////
	
	@POST
    @Path("/aarticle")
    @Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public TResult<Boolean> addArticle(
			final TArticle obj,
			@HeaderParam("AuthorizationKey") String sessionKey,
			@Context HttpServletRequest request){		
		return this.excecute(sessionKey, new IJxRsExecute() {
			
			@Override
			public TResult<Boolean> run(TSession session) throws SpiinPiinBaseException {
				// TODO Auto-generated method stub
				return null;
			}
		});				
	}
	
	@POST
    @Path("/acomment")
    @Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public TResult<Boolean> addComment(
			final TComment obj,
			@HeaderParam("AuthorizationKey") String sessionKey,
			@Context HttpServletRequest request){
		return this.excecute(sessionKey, new IJxRsExecute() {
			
			@SuppressWarnings("unchecked")
			@Override
			public TResult<Boolean> run(TSession session) throws SpiinPiinBaseException {
				// TODO Auto-generated method stub
				return null;
			}
		});	
	}
			
	private <T> T excecute(String sessionKey, IJxRsExecute run){			
		try {			
			TSession session = this.insMgt.getSession(sessionKey);
			
			if (session == null){
				logger.log(Level.SEVERE, "Session {" + sessionKey+ "} is invalid.");
				Response response = Response.status(
						Response.Status.UNAUTHORIZED).entity(
								"Session {" + sessionKey+ "} is invalid.").build();
				
				throw new WebApplicationException(response);
			}
			
			return run.run(session);
		} catch (SpiinPiinBaseException e) {
			logger.log(Level.SEVERE, e.getMessage(), e);
			Response response = Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(e.getMessage()).build();
			
			throw new WebApplicationException(response);
		}
	}
	
}