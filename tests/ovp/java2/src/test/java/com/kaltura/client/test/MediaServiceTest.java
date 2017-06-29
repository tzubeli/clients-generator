// ===================================================================================================
//                           _  __     _ _
//                          | |/ /__ _| | |_ _  _ _ _ __ _
//                          | ' </ _` | |  _| || | '_/ _` |
//                          |_|\_\__,_|_|\__|\_,_|_| \__,_|
//
// This file is part of the Kaltura Collaborative Media Suite which allows users
// to do with audio, video, and animation what Wiki platfroms allow them to do with
// text.
//
// Copyright (C) 2006-2011  Kaltura Inc.
//
// This program is free software: you can redistribute it and/or modify
// it under the terms of the GNU Affero General Public License as
// published by the Free Software Foundation, either version 3 of the
// License, or (at your option) any later version.
//
// This program is distributed in the hope that it will be useful,
// but WITHOUT ANY WARRANTY; without even the implied warranty of
// MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
// GNU Affero General Public License for more details.
//
// You should have received a copy of the GNU Affero General Public License
// along with this program.  If not, see <http://www.gnu.org/licenses/>.
//
// @ignore
// ===================================================================================================
package com.kaltura.client.test;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Stream;

import com.kaltura.client.types.APIException;
import com.kaltura.client.APIOkRequestsExecutor;
import com.kaltura.client.Client;
import com.kaltura.client.enums.EntryStatus;
import com.kaltura.client.enums.EntryType;
import com.kaltura.client.enums.MediaType;
import com.kaltura.client.enums.ModerationFlagType;
import com.kaltura.client.services.DataService;
import com.kaltura.client.services.FlavorAssetService;
import com.kaltura.client.services.MediaService;
import com.kaltura.client.services.PlaylistService;
import com.kaltura.client.services.UploadTokenService;
import com.kaltura.client.types.BaseEntry;
import com.kaltura.client.types.DataEntry;
import com.kaltura.client.types.FlavorAsset;
import com.kaltura.client.types.MediaEntry;
import com.kaltura.client.types.MediaEntryFilter;
import com.kaltura.client.types.MediaEntryFilterForPlaylist;
import com.kaltura.client.types.ListResponse;
import com.kaltura.client.types.ModerationFlag;
import com.kaltura.client.types.UploadToken;
import com.kaltura.client.types.UploadedFileTokenResource;
import com.kaltura.client.utils.request.ArrayRequestBuilder;
import com.kaltura.client.utils.request.RequestBuilder;
import com.kaltura.client.utils.response.OnCompletion;
import com.kaltura.client.ILogger;
import com.kaltura.client.Logger;

public class MediaServiceTest extends BaseTest {

	private ILogger logger = Logger.getLogger(MediaServiceTest.class);
	
//	/**
//	 * Tests the following : 
//	 * Media Service -
//	 *  - add From Url
//	 * @throws Exception 
//	 */
//	public void testAddFromUrl() throws Exception {
//		if (logger.isEnabled()) 
//			logger.info("Test Add From URL");
//        
//		final String name = "test (" + new Date() + ")";
//		
//		startUserSession();
//		addClipFromUrl(name, new OnCompletion<MediaEntry>() {
//
//			@Override
//			public void onComplete(MediaEntry addedEntry, APIException error) {
//				completion.assertNull(error);
//				
//				completion.assertNotNull(addedEntry);
//				completion.assertNotNull(addedEntry.getId());
//				completion.assertEquals(name, addedEntry.getName());
//				completion.assertEquals(EntryStatus.IMPORT, addedEntry.getStatus());
//				
//			}
//		});
//	}
//	
//	public void addClipFromUrl(String name, final OnCompletion<MediaEntry> onCompletion) {
//
//		MediaEntry entry = new MediaEntry();
//
//		entry.setName(name);
//		entry.setType(EntryType.MEDIA_CLIP);
//		entry.setMediaType(MediaType.VIDEO);
//
//		RequestBuilder<MediaEntry> requestBuilder = MediaService.addFromUrl(entry, testConfig.getTestUrl())
//		.setCompletion(new OnCompletion<MediaEntry>() {
//
//			@Override
//			public void onComplete(MediaEntry response, APIException error) {
//				completion.assertNull(error);
//
//				if(response != null)
//					testIds.add(response.getId());
//				
//				onCompletion.onComplete(response, error);
//			}
//		});
//		APIOkRequestsExecutor.getSingleton().queue(requestBuilder.build(client));
//	}
//	
//	/**
//	 * Tests the following : 
//	 * Media Service - 
//	 * 	- add 
//	 *  - add Content
//	 *  - count
//	 * Upload token - 
//	 *  - add
//	 *  - upload
//	 * Flavor asset - 
//	 * 	- get by entry id
//	 * @throws Exception 
//	 */
//	public void testUploadTokenAddGivenFile() throws Exception {
//		
//		if (logger.isEnabled())
//			logger.info("Test upload token add");
//		
//		final InputStream fileData = TestUtils.getTestVideo();
//		final int fileSize = fileData.available();
//		final String uniqueTag = "test_" + getUniqueString();
//
//		final MediaEntryFilter filter = new MediaEntryFilter();
//		filter.setTagsLike(uniqueTag);
//		
//		startUserSession();
//		RequestBuilder<Integer> requestBuilder = MediaService.count(filter)
//		.setCompletion(new OnCompletion<Integer>() {
//
//			@Override
//			public void onComplete(Integer response, APIException error) {
//				completion.assertNull(error);
//				
//				final int size = response;
//				
//
//				// Create entry
//				MediaEntry entry = new MediaEntry();
//				entry.setName("test (" + new Date() + ")");
//				entry.setType(EntryType.MEDIA_CLIP);
//				entry.setMediaType(MediaType.VIDEO);
//				entry.setTags(uniqueTag);
//				
//				RequestBuilder<MediaEntry> requestBuilder = MediaService.add(entry)
//				.setCompletion(new OnCompletion<MediaEntry>() {
//
//					@Override
//					public void onComplete(final MediaEntry entry, APIException error) {
//						completion.assertNull(error);
//						completion.assertNotNull(entry);
//						
//						testIds.add(entry.getId());
//						
//						// Create token
//						UploadToken uploadToken = new UploadToken();
//						uploadToken.setFileName(testConfig.getUploadVideo());
//						uploadToken.setFileSize((double) fileSize);
//
//						RequestBuilder<UploadToken> requestBuilder = UploadTokenService.add(uploadToken)
//						.setCompletion(new OnCompletion<UploadToken>() {
//
//							@Override
//							public void onComplete(final UploadToken token, APIException error) {
//								completion.assertNull(error);
//								completion.assertNotNull(token);
//								
//								// Define content
//								UploadedFileTokenResource resource = new UploadedFileTokenResource();
//								resource.setToken(token.getId());
//
//								RequestBuilder<MediaEntry> requestBuilder = MediaService.addContent(entry.getId(), resource)
//								.setCompletion(new OnCompletion<MediaEntry>() {
//
//									@Override
//									public void onComplete(final MediaEntry entry, APIException error) {
//										completion.assertNull(error);
//										completion.assertNotNull(entry);
//										
//										// upload
//										RequestBuilder<UploadToken> requestBuilder = UploadTokenService.upload(token.getId(), fileData, testConfig.getUploadVideo(), fileSize, false)
//										.setCompletion(new OnCompletion<UploadToken>() {
//
//											@Override
//											public void onComplete(UploadToken uploadToken, APIException error) {
//												completion.assertNull(error);
//												completion.assertNotNull(uploadToken);
//												
//												// Test Creation
//												getProcessedEntry(entry.getId(), true, new OnCompletion<MediaEntry>() {
//
//													@Override
//													public void onComplete(MediaEntry entry, APIException error) {
//														completion.assertNull(error);
//														completion.assertNotNull(entry);
//														
//														// Test get flavor asset by entry id.
//														RequestBuilder<List<FlavorAsset>> requestBuilder = FlavorAssetService.getByEntryId(entry.getId())
//														.setCompletion(new OnCompletion<List<FlavorAsset>>() {
//
//															@Override
//															public void onComplete(List<FlavorAsset> listFlavors, APIException error) {
//																completion.assertNull(error);
//																completion.assertNotNull(listFlavors);
//																completion.assertTrue(listFlavors.size() >= 1); // Should contain at least the source
//																
//
//																RequestBuilder<Integer> requestBuilder = MediaService.count(filter)
//																.setCompletion(new OnCompletion<Integer>() {
//
//																	@Override
//																	public void onComplete(Integer response, APIException error) {
//																		completion.assertNull(error);
//																		
//																		int size2 = response;
//																		completion.assertTrue(size + 1 == size2);
//																	}
//																});
//																APIOkRequestsExecutor.getSingleton().queue(requestBuilder.build(client));
//															}
//														});
//														APIOkRequestsExecutor.getSingleton().queue(requestBuilder.build(client));
//													}
//												});
//											}
//										});
//										APIOkRequestsExecutor.getSingleton().queue(requestBuilder.build(client));
//									}
//								});
//								APIOkRequestsExecutor.getSingleton().queue(requestBuilder.build(client));
//							}
//						});
//						APIOkRequestsExecutor.getSingleton().queue(requestBuilder.build(client));
//					}
//				});
//				APIOkRequestsExecutor.getSingleton().queue(requestBuilder.build(client));
//			}
//		});
//		APIOkRequestsExecutor.getSingleton().queue(requestBuilder.build(client));
//	}
//	
//	public void testUploadUnexistingFile() throws Exception {
//		
//		File file = new File("src/test/resources/nonExistingfile.flv");
//		
//		startUserSession();
//		
//		// Create token
//		UploadToken uploadToken = new UploadToken();
//		uploadToken.setFileName(file.getName());
//		uploadToken.setFileSize((double) file.length());
//		RequestBuilder<UploadToken> requestBuilder = UploadTokenService.add(uploadToken)
//		.setCompletion(new OnCompletion<UploadToken>() {
//
//			@Override
//			public void onComplete(UploadToken response, APIException error) {
//				completion.assertNull(response);
//				completion.assertNotNull(error);
//				completion.assertTrue(error.getMessage().contains("is not readable or not a file"));
//			}
//		});
//		APIOkRequestsExecutor.getSingleton().queue(requestBuilder.build(client));
//	}
//	
//	
//	/**
//	 * Tests the following : 
//	 * Media Service -
//	 *  - add From Url
//	 * http://www.kaltura.org/how-update-supposed-work-api-v3
//	 * @throws Exception 
//	 */
//	public void testUpdate() throws Exception {
//		if (logger.isEnabled())
//			logger.info("Test Update Entry");
//		
//		String name = "test (" + new Date() + ")";
//		
//		startUserSession();
//		
//		addTestImage(name, new OnCompletion<MediaEntry>() {
//
//			@Override
//			public void onComplete(final MediaEntry addedEntry, APIException error) {
//				completion.assertNotNull(addedEntry);
//				completion.assertNotNull(addedEntry.getId());
//				
//				final String name2 = "test (" + new Date() + ")";
//				
//				MediaEntry updatedEntry = new MediaEntry();
//				updatedEntry.setName(name2);			
//				
//				RequestBuilder<MediaEntry> requestBuilder = MediaService.update(addedEntry.getId(), updatedEntry)
//				.setCompletion(new OnCompletion<MediaEntry>() {
//
//					@Override
//					public void onComplete(MediaEntry response, APIException error) {
//						completion.assertNull(error);
//
//						getProcessedEntry(addedEntry.getId(), true, new OnCompletion<MediaEntry>() {
//
//							@Override
//							public void onComplete(MediaEntry queriedEntry, APIException error) {
//								completion.assertEquals(name2, queriedEntry.getName());
//							}
//						});
//					}
//				});
//				APIOkRequestsExecutor.getSingleton().queue(requestBuilder.build(client));
//			}
//		});
//	}
//	
//	/**
//	 * Tests the following : 
//	 * Media Service -
//	 *  - Get
//	 * @throws Exception 
//	 */
//	public void testBadGet() throws Exception {
//		if (logger.isEnabled())
//			logger.info("Starting badGet test");
//		
//		// look for one we know doesn't exist
//		startUserSession();
//		RequestBuilder<MediaEntry> requestBuilder = MediaService.get("bad-id")
//		.setCompletion(new OnCompletion<MediaEntry>() {
//
//			@Override
//			public void onComplete(MediaEntry response, APIException error) {
//				completion.assertNull(response);
//				completion.assertNotNull(error);
//			}
//		});
//		APIOkRequestsExecutor.getSingleton().queue(requestBuilder.build(client));
//	}
//	
//	/**
//	 * Tests the following : 
//	 * Media Service -
//	 *  - Get
//	 * @throws Exception 
//	 */
//	public void testGet() throws Exception {
//		if (logger.isEnabled())
//			logger.info("Starting get test");
//		
//		String name = "test (" + new Date() + ")";
//		
//		startUserSession();
//		addTestImage(name, new OnCompletion<MediaEntry>() {
//
//			@Override
//			public void onComplete(final MediaEntry addedEntry, APIException error) {
//				completion.assertNull(error);
//
//				RequestBuilder<MediaEntry> requestBuilder = MediaService.get(addedEntry.getId())
//				.setCompletion(new OnCompletion<MediaEntry>() {
//
//					@Override
//					public void onComplete(MediaEntry retrievedEntry, APIException error) {
//						completion.assertNull(error);
//
//						completion.assertNotNull(retrievedEntry);
//						completion.assertEquals(addedEntry.getId(), retrievedEntry.getId());
//					}
//				});
//				APIOkRequestsExecutor.getSingleton().queue(requestBuilder.build(client));
//			}
//		});
//	}
//
//	/**
//	 * Tests the following : 
//	 * Media Service -
//	 *  - list
//	 * @throws Exception 
//	 */
//	public void testList() throws Exception {
//
//		if (logger.isEnabled())
//			logger.info("Test List");
//
//		final int count = 2;
//		
//		startUserSession();
//		
//		OnCompletion<MediaEntry> onCompletion = new OnCompletion<MediaEntry>() {
//			List<MediaEntry> entries = new ArrayList<MediaEntry>();
//
//			@Override
//			public void onComplete(MediaEntry addedEntry, APIException error) {
//				completion.assertNull(error);
//
//				getProcessedEntry(addedEntry.getId(), true, new OnCompletion<MediaEntry>() {
//
//					@Override
//					public void onComplete(MediaEntry readyEntry, APIException error) {
//						completion.assertNull(error);
//						
//						entries.add(readyEntry);
//						if(entries.size() == count) {
//							testFilters();
//						}
//					}
//				});
//			}
//			
//			private void testFilters() {
//
//				Object[] nameObjects = entries.stream().map(new Function<MediaEntry, String>() {
//					@Override
//					public String apply(MediaEntry t) {
//						return t.getName();
//					}
//				})
//				.toArray();
//
//				String[] names = new String[nameObjects.length];
//				System.arraycopy(nameObjects, 0, names, 0, nameObjects.length);
//				
//				MediaEntryFilter filter = new MediaEntryFilter();
//				filter.setNameMultiLikeOr(String.join(",", names));
//
//				RequestBuilder<ListResponse<MediaEntry>> requestBuilder = MediaService.list(filter)
//				.setCompletion(new OnCompletion<ListResponse<MediaEntry>>() {
//
//					@Override
//					public void onComplete(ListResponse<MediaEntry> listResponse, APIException error) {
//						completion.assertNull(error);
//
//						completion.assertEquals(listResponse.getTotalCount(), count);
//
//						for(final MediaEntry entry : entries) {
//							long foundItems = listResponse
//							.getObjects()
//							.stream()
//							.filter(new Predicate<MediaEntry>() {
//
//								@Override
//								public boolean test(MediaEntry t) {
//									return (t.getId() == entry.getId());
//								}
//							})
//							.count();
//							
//
//							completion.assertEquals(1, foundItems);
//						}
//					}
//				});
//				APIOkRequestsExecutor.getSingleton().queue(requestBuilder.build(client));
//			}
//		};
//		
//		for(int i = 0; i < count; i++) {
//			// add test clips
//			String name = "test one (" + new Date() + ")";
//			addTestImage(name, onCompletion);
//			Thread.sleep(1000);
//		}
//	}
	
	/**
	 * Tests the following : 
	 * Media Service -
	 *  - flag
	 *  - list flags
	 * @throws Exception 
	 */
	public void testModeration() throws Exception {
		
		if (logger.isEnabled())
			logger.info("Starting moderation test");
		
		final String FLAG_COMMENTS = "This is a test flag";
		final String name = "test (" + new Date() + ")";
		
		startAdminSession();

		final Completion completion = new Completion();
		completion.run(new Runnable() {
			@Override
			public void run() {
				addTestImage(completion, name, new OnCompletion<MediaEntry>() {

					@Override
					public void onComplete(MediaEntry addedEntry, APIException error) {

						if (logger.isEnabled())
							logger.info("Entry added: " + addedEntry.getId());
						
						//wait for the newly-added clip to process
						getProcessedEntry(completion, addedEntry.getId(), new OnCompletion<MediaEntry>() {

							@Override
							public void onComplete(final MediaEntry addedEntry, APIException error) {
								
								// flag the clip
								ModerationFlag flag = new ModerationFlag();
								flag.setFlaggedEntryId(addedEntry.getId());
								flag.setFlagType(ModerationFlagType.SPAM_COMMERCIALS);
								flag.setComments(FLAG_COMMENTS);
								RequestBuilder<ModerationFlag> requestBuilder = MediaService.flag(flag)
								.setCompletion(new OnCompletion<ModerationFlag>() {

									@Override
									public void onComplete(ModerationFlag response, APIException error) {
										completion.assertNull(error);

										// get the list of flags for this entry
										RequestBuilder<ListResponse<ModerationFlag>> requestBuilder = MediaService.listFlags(addedEntry.getId())
										.setCompletion(new OnCompletion<ListResponse<ModerationFlag>>() {

											@Override
											public void onComplete(ListResponse<ModerationFlag> flagList, APIException error) {
												completion.assertNull(error);

												completion.assertEquals(flagList.getTotalCount(), 1);

												// check that the flag we put in is the flag we got back
												ModerationFlag retFlag = flagList.getObjects().get(0);						
												completion.assertEquals(retFlag.getFlagType(), ModerationFlagType.SPAM_COMMERCIALS);
												completion.assertEquals(retFlag.getComments(), FLAG_COMMENTS);
												
												completion.complete();
											}
										});
										APIOkRequestsExecutor.getSingleton().queue(requestBuilder.build(client));
									}
								});
								APIOkRequestsExecutor.getSingleton().queue(requestBuilder.build(client));
							}
						});
					}
				});
			}
		});
	}
	
//	
//	/**
//	 * Tests the following : 
//	 * Media Service -
//	 *  - delete
//	 * @throws IOException 
//	 */
//	public void testDelete() throws Exception {
//		if (logger.isEnabled())
//			logger.info("Starting delete test");
//		
//		String name = "test (" + new Date() + ")";
//		
//		startUserSession();
//		
//		// First delete - should succeed
//		addTestImage(name, new OnCompletion<MediaEntry>() {
//
//			@Override
//			public void onComplete(MediaEntry addedEntry, APIException error) {
//				completion.assertNotNull(addedEntry);
//				final String idToDelete = addedEntry.getId();
//				
//				// calling this makes the test wait for processing to complete
//				// if you call delete while it is processing, the delete doesn't happen
//				getProcessedEntry(idToDelete, new OnCompletion<MediaEntry>() {
//
//					@Override
//					public void onComplete(MediaEntry response, APIException error) {
//						completion.assertNull(error);
//						
//						RequestBuilder<Void> requestBuilder = MediaService.delete(idToDelete)
//						.setCompletion(new OnCompletion<Void>() {
//
//							@Override
//							public void onComplete(Void response, APIException error) {
//								completion.assertNull(error);
//
//								// Get deleted - should fail
//								RequestBuilder<MediaEntry> requestBuilder1 = MediaService.get(idToDelete)
//								.setCompletion(new OnCompletion<MediaEntry>() {
//
//									@Override
//									public void onComplete(MediaEntry response, APIException error) {
//										completion.assertNotNull(error);
//									}
//								});
//								
//								// Second delete - should fail
//								RequestBuilder<Void> requestBuilder2 = MediaService.delete(idToDelete)
//								.setCompletion(new OnCompletion<Void>() {
//
//									@Override
//									public void onComplete(Void response, APIException error) {
//										completion.assertNotNull(error);
//									}
//								});
//								
//								APIOkRequestsExecutor.getSingleton().queue(requestBuilder1.build(client));
//								APIOkRequestsExecutor.getSingleton().queue(requestBuilder2.build(client));
//								
//								// we whacked this one, so let's not keep track of it		
//								testIds.remove(testIds.size() - 1);
//							}
//						});
//						APIOkRequestsExecutor.getSingleton().queue(requestBuilder.build(client));
//					}
//				});
//				
//			}
//		});
//	}
//	
//	/**
//	 * Tests the following : 
//	 * Media Service -
//	 *  - upload
//	 *  - add from uploaded file
//	 * @throws Exception 
//	 */
//	public void testUpload() throws Exception {
//		if (logger.isEnabled())
//			logger.info("Starting delete test");
//
//		startUserSession();
//
//		InputStream fileData = TestUtils.getTestVideo();
//		int fileSize = fileData.available();
//
//		RequestBuilder<String> requestBuilder = MediaService.upload(fileData, testConfig.getUploadVideo(), fileSize)
//		.setCompletion(new OnCompletion<String>() {
//
//			@Override
//			public void onComplete(String result, APIException error) {
//				completion.assertNull(error);
//
//				if (logger.isEnabled())
//					logger.debug("After upload, result:" + result);		
//				
//				String name = "test (" + new Date() + ")";
//				MediaEntry entry = new MediaEntry();
//				entry.setName(name);
//				entry.setType(EntryType.MEDIA_CLIP);
//				entry.setMediaType(MediaType.VIDEO);
//				
//				RequestBuilder<MediaEntry> requestBuilder = MediaService.addFromUploadedFile(entry, result)
//				.setCompletion(new OnCompletion<MediaEntry>() {
//
//					@Override
//					public void onComplete(MediaEntry entry, APIException error) {
//						completion.assertNull(error);
//						completion.assertNotNull(entry);
//						completion.assertNotNull(entry.getId());
//						
//						testIds.add(entry.getId());
//					}
//				});
//				APIOkRequestsExecutor.getSingleton().queue(requestBuilder.build(client));
//			}
//		});
//		APIOkRequestsExecutor.getSingleton().queue(requestBuilder.build(client));
//	}
//	
//	public void testPlaylist() throws Exception {
//		if (logger.isEnabled())	
//			logger.info("Starting test playlist execute from filters");
//	
//		startAdminSession();
//		
//		// Create entry
//		addTestImage("test (" + new Date() + ")", new OnCompletion<MediaEntry>() {
//
//			@Override
//			public void onComplete(MediaEntry entry, APIException error) {
//				completion.assertNull(error);
//				
//				// generate filter
//				MediaEntryFilterForPlaylist filter = new MediaEntryFilterForPlaylist();
//				filter.setReferenceIdEqual(entry.getReferenceId());
//				List<MediaEntryFilterForPlaylist> filters = new ArrayList<MediaEntryFilterForPlaylist>();
//				filters.add(filter);
//				RequestBuilder<List<BaseEntry>> requestBuilder = PlaylistService.executeFromFilters(filters, 5)
//				.setCompletion(new OnCompletion<List<BaseEntry>>() {
//
//					@Override
//					public void onComplete(List<BaseEntry> res, APIException error) {
//						completion.assertNull(error);
//						completion.assertNotNull(res);
//						completion.assertEquals(1, res.size());
//					}
//				});
//				APIOkRequestsExecutor.getSingleton().queue(requestBuilder.build(client));
//			}
//		});
//	}
//	
//	public void testServe() throws Exception {
//		final String test = "bla bla bla";
//		
//		startUserSession();
//		
//		// Add Entry
//		DataEntry dataEntry = new DataEntry();
//		dataEntry.setName("test (" + new Date() + ")");
//		dataEntry.setDataContent(test);
//		RequestBuilder<DataEntry> requestBuilder = DataService.add(dataEntry)
//		.setCompletion(new OnCompletion<DataEntry>() {
//
//			@Override
//			public void onComplete(DataEntry addedDataEntry, APIException error) {
//				completion.assertNull(error);
//
//				// serve
//				RequestBuilder<String> requestBuilder = DataService.serve(addedDataEntry.getId())
//				.setCompletion(new OnCompletion<String>() {
//
//					@Override
//					public void onComplete(String response, APIException error) {
//						completion.assertEquals(test, response);
//					}
//				});
//				APIOkRequestsExecutor.getSingleton().queue(requestBuilder.build(client));
//			}
//		});
//		APIOkRequestsExecutor.getSingleton().queue(requestBuilder.build(client));
//	}
//	
//	private String readContent(URL url) {
//		StringBuffer sb = new StringBuffer();
//		BufferedReader in = null;
//		try {
//			in = new BufferedReader(new InputStreamReader(url.openStream()));
//			String inputLine;
//
//			while ((inputLine = in.readLine()) != null)
//				sb.append(inputLine);
//
//			
//		} catch (IOException e) {
//			e.printStackTrace();
//			fail(e.getMessage());
//		} finally {
//			if(in != null)
//				try {
//					in.close();
//				} catch (IOException e) {
//					fail(e.getMessage());
//				}
//		}
//
//		return sb.toString();
//	}
	
}
